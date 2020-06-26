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

fun <T : BaseScope> service(
    scope: T,
    createRegistry: T.() -> HandlerRegistry
): Connectable =
    ScopedService(scope, scope.createRegistry())

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

    fun Connector.handleSubscription(subscription: Subscription) {
        if (!subscription.enable)
            subscriptions.remove(subscription::class)?.cancel()
        else
            subscriptions.getOrPut(subscription::class) {
                handlers.dispatch(subscription, output)
                    ?: throw IllegalArgumentException("Handler for $subscription must return Job")
            }
    }

    suspend fun Connector.handleRequest(request: Any) {
        handlers.dispatch(request, output)?.join()
    }
}
