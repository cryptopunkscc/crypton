package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.util.TypedLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
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

    private val subscriptions = mutableMapOf<KClass<*>, Job>()

    override fun Connector.connect(): Job = launch {
        val log = coroutineContext[TypedLog]!!
        log.d("Start $id")
        input.collect {
            log.d("$id Received $it")
            when (it) {
                is Subscription -> handleSubscription(it)
                else -> handleRequest(it)
            }
        }
        subscriptions.values.forEach { it.cancel() }
        subscriptions.clear()
    }

    private fun Connector.handleSubscription(subscription: Subscription) {
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
