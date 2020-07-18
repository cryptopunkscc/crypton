package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.util.TypedLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import java.util.concurrent.CancellationException
import kotlin.reflect.KClass

fun <T : CoroutineScope> T.connectable(
    createRegistry: HandlerRegistryFactory<T>
): Connectable =
    ScopedConnectable(this, createRegistry())

typealias HandlerRegistryFactory<T> = T.() -> HandlerRegistry

private data class ScopedConnectable(
    val scope: CoroutineScope,
    val handlers: HandlerRegistry
) :
    Connectable,
    CoroutineScope by scope {

    override fun Connector.connect(): Job = launch {
        val subscriptions: MutableMap<KClass<*>, Job> = mutableMapOf()
        val log = coroutineContext[TypedLog]!!
        log.d("Start $id")
        input.onCompletion { throwable ->
            subscriptions.values.forEach { it.cancel(CancellationException("Complete input $it $throwable")) }
            subscriptions.clear()
        }.collect {
            log.d("$id Received $it")
            when (it) {
                is Subscription -> handleSubscription(subscriptions, it)
                else -> handleRequest(it)
            }
        }
    }

    private fun Connector.handleSubscription(
        subscriptions: MutableMap<KClass<*>, Job>,
        subscription: Subscription
    ) {
        if (!subscription.enable)
            subscriptions.remove(subscription::class)?.cancel()
        else
            subscriptions.getOrPut(subscription::class) {
                handlers.dispatch(subscription, output)
                    ?: throw IllegalArgumentException("Handler for $subscription must return Job")
            }
    }

    private suspend fun Connector.handleRequest(request: Any) {
        handlers.dispatch(request, output)?.join()
    }
}
