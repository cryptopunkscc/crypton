package cc.cryptopunks.crypton

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.lang.ClassCastException
import java.util.*
import java.util.concurrent.CancellationException
import kotlin.reflect.KClass

fun <T : Scope> T.connectable(): Connectable = ScopedConnectable(this)

private data class ScopedConnectable(
    val scope: Scope
) :
    Connectable,
    CoroutineScope by scope {

    override fun Connector.connect(): Job = launch {
        val subscriptions: MutableMap<KClass<*>, Job> = mutableMapOf()
        val async: WeakHashMap<Job, Any> = WeakHashMap()
        scope.log.d("Start connectable $scope")
        input.onCompletion { throwable ->
            scope.log.d("Stop connectable $scope")
            subscriptions.values.forEach { it.cancel(CancellationException("Complete input $it $throwable")) }
            subscriptions.clear()
        }.onEach {
            scope.log.d("Input $it")
        }.map { arg ->
            when (arg) {
                is Context -> scope.resolve(arg)
                else -> scope to arg
            }
        }.collect { (scope, arg) ->
            try {
                scope.log.d("connectable received $arg")
                when (arg) {
                    is Subscription -> scope.handleSubscription(subscriptions, arg, output)
                    is Async -> scope.handleRequest(arg, output).also { async[it] = arg }
                    else -> scope.handleRequest(arg, output).join()
                }
            } catch (e: Throwable) {
                ActionError(e.message ?: e.javaClass.name, arg.toString()).also {
                    e.printStackTrace()
                    output(it)
                }
            } finally {
                scope.log.d("connectable finished $arg")
                scope.log.d("subscriptions: \n${subscriptions.keys.joinToString("\n")}")
                scope.log.d("async: \n${async.toList().joinToString("\n") {(key, value) -> "$value completed: ${key.isCompleted}" }}")
            }
        }
        scope.log.d("Stop connectable $scope")
    }
}

private fun Scope.handleSubscription(
    subscriptions: MutableMap<KClass<*>, Job>,
    subscription: Subscription,
    output: ConnectorOutput
) {
    log.d("handle subscription $subscription")
    if (!subscription.enable) subscriptions.remove(subscription::class)?.cancel()
    else subscriptions.getOrPut(subscription::class) {
        handleRequest(subscription, output)
    }
}

private fun Scope.handleRequest(any: Any, output: ConnectorOutput = {}): Job =
    handlerFor(any)?.let { handle ->
        launch {
            try {
                this@handleRequest.handle(output, any)
            } catch (e: ClassCastException) {
                throw Exception("Cannot execute $any on $this", e)
            }
        }
    } ?: throw Exception(
        "No register handler for $any \n${handlers.keys.joinToString("\n") {
            it.toString().replace("class cc.cryptopunks.crypton.context.", "").replace("$", ".")
        }}"
    )
