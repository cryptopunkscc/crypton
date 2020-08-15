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
import kotlinx.coroutines.launch
import java.util.*
import kotlin.reflect.KClass

fun <T : Scope> T.service(name: String = "NoName.service"): Connectable = Service(name, this)

val Any.serviceName get() = "${javaClass.simpleName}.service"
val String.serviceName get() = "$this.service"

private data class Service(
    val name: String,
    val scope: Scope
) :
    Connectable,
    CoroutineScope by scope {

    override val coroutineContext = CoroutineLog.Label(name) + scope.coroutineContext

    override fun Connector.connect(): Job = launch {
        val subs: MutableMap<KClass<*>, Job> = mutableMapOf()
        val async: WeakHashMap<Job, Any> = WeakHashMap()

        input
            .onStart {
                logConnectionStarted()
            }
            .onCompletion { throwable ->

                logConnectionFinished(throwable)
                (async.keys + subs.values).forEach { job ->
                    job.cancel(CancellationException("Complete input $job $throwable"))
                }
                async.clear()
                subs.clear()
            }
            .collect { arg ->
                when (arg) {
                    is Context -> runCatching {
                        scope.resolve(arg)
                    }.getOrElse {

                        scope to CannotResolve(arg)
                    }
                    else -> {
                        scope to arg
                    }
                }.let { (scope, action) ->
                    when (action) {
                        is Subscription -> scope.handleSubscription(subs, action, output)
                        is Async -> scope.handleAsync(async, action, output)
                        is Failure -> action.out()
                        else -> scope.handleRequest(action, output).join()
                    }
                }
            }
    }
}

private fun Scope.handleSubscription(
    subscriptions: MutableMap<KClass<*>, Job>,
    subscription: Subscription,
    out: Output
) {
    if (!subscription.enable) subscriptions.remove(subscription::class)?.cancel()
    else subscriptions.getOrPut(subscription::class) {
        handleRequest(subscription, out)
    }
}

private fun Scope.handleAsync(
    async: WeakHashMap<Job, Any>,
    action: Async,
    out: Output
) {
    handleRequest(action, out).also {
        async[it] = action
    }
}

private fun Scope.handleRequest(
    action: Any,
    out: Output = {}
): Job = launch(
    CoroutineLog.Action(action) +
        CoroutineLog.Status(Log.Event.Status.Handling)
) {
    handlerFor(action)
        ?.let { handle ->
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
            logNoHandlersFor(action)
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

private suspend fun Scope.logNoHandlersFor(action: Any) = log.e {
    "No register handler for $action\n" + handlers.keys.joinToString("\n") {
        it.toString()
            .replace("class cc.cryptopunks.crypton.context.", "")
            .replace("$", ".")
    }
}
