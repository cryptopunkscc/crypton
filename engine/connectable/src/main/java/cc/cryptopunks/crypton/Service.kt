package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.util.Log
import cc.cryptopunks.crypton.util.logger.CoroutineLog
import cc.cryptopunks.crypton.util.logger.log
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import java.io.PrintWriter
import java.io.StringWriter
import java.util.*
import kotlin.reflect.KClass

fun <T : Scope> T.service(
    name: String = "NoName.service"
): Connectable = Service(
    name = name,
    scope = this,
    resolvers = resolvers
)

val Any.serviceName get() = "${javaClass.simpleName}.service"
val String.serviceName get() = "$this.service"

private data class Service(
    val name: String,
    val scope: Scope,
    val resolvers: Resolvers = Resolvers(),
) :
    Connectable,
    CoroutineScope by scope {

    data class Connection(
        val scope: Scope,
        val connector: Connector,
        val resolvers: Resolvers,
        val subs: MutableMap<KClass<*>, Job> = mutableMapOf(),
        val async: WeakHashMap<Job, Any> = WeakHashMap()
    ) {
        val out: Output get() = connector.output
    }

    override val coroutineContext = CoroutineLog.Label(name) + scope.coroutineContext

    override fun Connector.connect(): Job = launch {
        Connection(
            scope = scope,
            connector = this@connect,
            resolvers = resolvers
        ).handle().joinAll()
    }
}

private suspend fun Service.Connection.handle() = connector.input
    .onStart {
        logConnectionStarted()
    }
    .onCompletion { throwable ->

        logConnectionFinished(throwable)

        if (throwable != null) {
            (async.keys + subs.values).forEach { job ->
                job.cancel(CancellationException("Complete input $job $throwable"))
            }
            async.clear()
            subs.clear()
        }
    }
    .collect { arg ->
        val (scope, action) = resolvers.resolve(scope, arg)
        handleAction(scope, action)
    }
    .let {
        async.keys + subs.values
    }

private suspend fun Service.Connection.handleAction(scope: Scope, action: Any) {
    when (action) {
        is Subscription -> scope.handleSubscription(subs, action, out)
        is Async -> scope.handleAsync(async, action, out)
        is Failure -> action.out()
        else -> scope.handleRequest(action, out).join()
    }
}

private fun Scope.handleSubscription(
    subscriptions: MutableMap<KClass<*>, Job>,
    subscription: Subscription,
    out: Output
) {
    val type = subscription::class
    if (!subscription.enable) subscriptions.remove(type)?.cancel()
    else subscriptions.getOrPut(type) {
        handleRequest(subscription, out).apply {
            invokeOnCompletion { subscriptions -= type }
        }
    }
}

private fun Scope.handleAsync(
    async: WeakHashMap<Job, Any>,
    action: Async,
    out: Output
) {
    handleRequest(action, out).also { job ->
        job.invokeOnCompletion { async -= job }
        async[job] = action
    }
}

private fun Scope.handleRequest(
    action: Any,
    out: Output = {}
): Job = launch(
    CoroutineLog.Action(action) +
        CoroutineLog.Status(Log.Event.Status.Handling)
) {
    val handlers = handlers

    handlers[action::class]
        ?.let { handle ->
            log.builder.d {
                status = Log.Event.Status.Start.name
            }
            runCatching {
                handle(out, action)
                log.builder.d {
                    status = Log.Event.Status.Finished.name
                }
            }.getOrElse { e ->
                ActionFailed(action, e).out()
                log.builder.e {
                    status = Log.Event.Status.Failed.name
                    throwable = e
                }
            }
        }
        ?: InvalidAction(action, handlers.keys).out().also {
            handlers.logNoHandlersFor(action)
        }
}

private suspend fun logConnectionStarted() = log.builder.d {
    status = Log.Event.Status.Start.name
    message = "Connection"
}

private suspend fun logConnectionFinished(e: Throwable?) = log.builder.d {
    status = Log.Event.Status.Finished.name
    message = "Connection"
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
    val availableActions: List<String>
) : Failure {
    constructor(action: Any, availableActions: Collection<KClass<*>>) : this(
        action = action.javaClass.name,
        availableActions = availableActions.map { it.java.name }
    )
}

data class CannotResolve(
    val message: String
) : Failure {
    constructor(context: Context) : this("context: ${context.id}")
    constructor(any: Any) : this(any.toString())
}

data class ActionFailed(
    val action: String,
    val stackTrace: String,
    val message: String?
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

