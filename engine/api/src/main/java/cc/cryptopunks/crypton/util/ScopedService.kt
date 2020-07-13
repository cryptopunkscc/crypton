package cc.cryptopunks.crypton.util

import cc.cryptopunks.crypton.context.BaseScope
import cc.cryptopunks.crypton.context.Connectable
import cc.cryptopunks.crypton.context.Connector
import cc.cryptopunks.crypton.context.HandlerRegistry
import cc.cryptopunks.crypton.context.Subscription
import cc.cryptopunks.crypton.context.dispatch
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

fun <T : BaseScope> T.service(
    createRegistry: HandlerRegistryFactory<T>
): Connectable =
    ScopedService(this, createRegistry())

typealias HandlerRegistryFactory<T> = T.() -> HandlerRegistry

private data class ScopedService(
    val scope: BaseScope,
    val handlers: HandlerRegistry
) :
    Connectable,
    BaseScope by scope {

    private val subscriptions = mutableMapOf<KClass<*>, Job>()

    override fun Connector.connect(): Job = launch {
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
