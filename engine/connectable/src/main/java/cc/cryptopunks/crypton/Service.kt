package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.util.Log
import cc.cryptopunks.crypton.util.logger.CoroutineLog
import cc.cryptopunks.crypton.util.logger.log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.CancellationException
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
        val subscriptions: MutableMap<KClass<*>, Job> = mutableMapOf()
        val async: WeakHashMap<Job, Any> = WeakHashMap()

        input.onStart {
            log.builder.d {
                status = Log.Event.Status.Start.name
                message = "connectable"
            }
        }.onCompletion { throwable ->
            log.builder.d {
                status = Log.Event.Status.Finished.name
                message = "connectable"
            }
            subscriptions.values.forEach { it.cancel(CancellationException("Complete input $it $throwable")) }
            subscriptions.clear()
        }.map { arg ->
            when (arg) {
                is Context -> scope.resolve(arg)
                else -> scope to arg
            }
        }.collect { (scope, arg) ->
            try {
                when (arg) {
                    is Subscription -> scope.handleSubscription(subscriptions, arg, output)
                    is Async -> scope.handleRequest(arg, output).also { async[it] = arg }
                    else -> scope.handleRequest(arg, output).join()
                }
            } catch (e: Throwable) {
                Action.Error(e.message ?: e.javaClass.name, arg.toString()).also {
                    e.printStackTrace()
                    output(it)
                }
            }
        }
    }
}

private fun Scope.handleSubscription(
    subscriptions: MutableMap<KClass<*>, Job>,
    subscription: Subscription,
    output: Output
) {
    if (!subscription.enable) subscriptions.remove(subscription::class)?.cancel()
    else subscriptions.getOrPut(subscription::class) {
        handleRequest(subscription, output)
    }
}

private fun Scope.handleRequest(
    action: Any,
    output: Output = {}
): Job =
    handlerFor(action)?.let { handle ->
        launch(
            CoroutineLog.Action(action) + CoroutineLog.Status(Log.Event.Status.Handling)
        ) {
            try {
                this@handleRequest.handle(output, action)
                log.builder.d { status = Log.Event.Status.Finished.name }
            } catch (e: ClassCastException) {
                throw Exception("Cannot execute $action on $this", e)
            }
        }
    } ?: throw Exception(
        "No register handler for $action \n${handlers.keys.joinToString("\n") {
            it.toString().replace("class cc.cryptopunks.crypton.context.", "").replace("$", ".")
        }}"
    )
