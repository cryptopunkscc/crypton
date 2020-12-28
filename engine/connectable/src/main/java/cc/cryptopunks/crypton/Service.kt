package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.util.Log
import cc.cryptopunks.crypton.util.logger.CoroutineLog
import cc.cryptopunks.crypton.util.logger.log
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import java.io.PrintWriter
import java.io.StringWriter
import java.util.*
import kotlin.coroutines.coroutineContext
import kotlin.reflect.KClass

fun <T : CoroutineScope> T.service(
    name: String = "NoName.service",
): Connectable = Service(
    name = name,
    scope = this
)

val Any.serviceName get() = "${javaClass.simpleName}.service"
val String.serviceName get() = "$this.service"

private data class Service(
    val name: String,
    val scope: Scope,
) :
    Connectable {

    data class Connection(
        val scope: CoroutineScope,
        val connector: Connector,
        val handlers: Handlers = scope.handlers,
        val resolvers: Resolvers = scope.resolvers,
        val subs: MutableMap<KClass<*>, Job> = mutableMapOf(),
        val async: WeakHashMap<Job, Any> = WeakHashMap(),
    ) {
        val out: Output get() = connector.output
    }

    override val coroutineContext = CoroutineLog.Label(name) +
        scope.coroutineContext +
        SupervisorJob(scope.coroutineContext[Job])

    override fun Connector.connect(): Job {
        val connection = Connection(
            scope = this@Service,
            connector = this@connect,
            resolvers = resolvers
        )
        return launch {
//            logConnectionStarted(this@Service.coroutineContext[ScopeTag])
            connection.handle().joinAll()
        }.apply {
            invokeOnCompletion {
                connection.apply {
                    (async.keys + subs.values).forEach { job ->
                        job.cancel()
//                        job.cancel(CancellationException("Complete input $job $it"))
                    }
                    async.clear()
                    subs.clear()
                }
            }
        }
    }
}

suspend fun Connector.connectService() = coroutineScope {
    Service.Connection(
        scope = this,
        connector = this@connectService,
    ).handle().joinAll()
}

private suspend fun Service.Connection.handle() = connector.input
    .onStart {
        logConnectionStarted()
    }
//    .onCompletion { throwable ->
//
//        logConnectionFinished(throwable)
//
//        if (throwable != null) {
//            (async.keys + subs.values).forEach { job ->
////                job.cancel(CancellationException("Complete input $job $throwable"))
//                job.cancel()
//            }
//            async.clear()
//            subs.clear()
//        }
//    }
    .collect { arg ->
        log.d { "Start handling: $arg, job: ${scope.coroutineContext[Job]}, tag: ${scope.coroutineContext[ScopeTag]}" }
        try {
            val (
                scope,
                action,
            ) = resolvers.resolve(scope, arg)
            copy(
                scope = scope + cryptonContext(
                    CoroutineLog.Action(action),
                    CoroutineLog.Status(Log.Event.Status.Handling)
                )
            ).handleAction(action)
        } catch (e: Throwable) {
            log.builder.e { throwable = e }
        }
    }
    .let {
        async.keys + subs.values
    }

private suspend fun Service.Connection.handleAction(action: Any) {
    when (action) {
        is Subscription -> handleSubscription(subs, action, out)
        is Async -> handleAsync(async, action, out)
        is Failure -> action.out()
        else -> handleRequest(action, out).join()
    }
}

private fun Service.Connection.handleSubscription(
    subscriptions: MutableMap<KClass<*>, Job>,
    subscription: Subscription,
    out: Output,
) {
    val type = subscription::class
    if (!subscription.enable) subscriptions.remove(type)?.cancel()
    else subscriptions.getOrPut(type) {
        handleRequest(subscription, out).apply {
            invokeOnCompletion { subscriptions -= type }
        }
    }
}

private fun Service.Connection.handleAsync(
    async: WeakHashMap<Job, Any>,
    action: Async,
    out: Output,
) {
    handleRequest(action, out).also { job ->
        job.invokeOnCompletion { async -= job }
        async[job] = action
    }
}

private fun Service.Connection.handleRequest(
    action: Any,
    out: Output = {},
): Job = scope.launch {

    this@handleRequest.handlers[action::class]
        ?.let { handle ->
            log.builder.d {
                status = Log.Event.Status.Start.name
            }
            runCatching {
                handle(out, action)
                log.builder.d {
                    status = Log.Event.Status.Finished.name
                }
            }.onFailure { e ->
                if (e !is CancellationException)
                    runCatching {
                        log.builder.e {
                            status = Log.Event.Status.Failed.name
                            throwable = e
                        }
                        ActionFailed(action, e).out()
                    }.onFailure {
                        log.builder.e {
                            status = Log.Event.Status.Failed.name
                            throwable = it
                        }
                    }
            }
        }
        ?: InvalidAction(action, handlers.keys).out().also {
            handlers.logNoHandlersFor(action)
        }
}

private suspend fun logConnectionStarted(tag: ScopeTag? = null) {
//    println("Start service connection: \n" + coroutineContext.elements().joinToString("") { "Start service connection: $it\n" })
    log.builder.d {
        status = Log.Event.Status.Start.name
        message = "Start service connection ${tag ?: coroutineContext[ScopeTag]}"
    }
}

private suspend fun logConnectionFinished(e: Throwable?) = log.builder.d {
    status = Log.Event.Status.Finished.name
    message = "Finish service connection ${coroutineContext[ScopeTag]}"
    throwable = e
}

private suspend fun HandlerRegistry.logNoHandlersFor(action: Any) = log.e {
    "No register handler for $action\n" + keys.joinToString("\n") {
        it.toString()
            .replace("class cc.cryptopunks.crypton.context.", "")
            .replace("$", ".")
    }
}


interface Failure

data class InvalidAction(
    val action: String,
    val availableActions: List<String>,
) : Failure {
    constructor(action: Any, availableActions: Collection<KClass<*>>) : this(
        action = action.javaClass.name,
        availableActions = availableActions.map { it.java.name }
    )
}

data class CannotResolve(
    val message: String,
) : Failure {
    constructor(context: Context) : this("context: ${context.id}")
    constructor(any: Any) : this(any.toString()) {
        println()
    }
}

data class ActionFailed(
    val action: String,
    val stackTrace: String,
    val message: String?,
) : Failure {
    constructor(action: Any, throwable: Throwable) : this(
        action = action.javaClass.name,
        message = throwable.message,
        stackTrace = StringWriter().also { throwable.printStackTrace(PrintWriter(it)) }.toString()
//        stackTrace = stringStackTrace(throwable)
    )
}

// TODO WTF error
private fun stringStackTrace(throwable: Throwable) =
    StringWriter().also { throwable.printStackTrace(PrintWriter(it)) }.toString()

