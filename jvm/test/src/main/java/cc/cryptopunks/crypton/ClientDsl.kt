package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.context.Connector
import cc.cryptopunks.crypton.util.TypedLog
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlin.coroutines.CoroutineContext

class ClientDsl(
    private val connector: Connector,
    val log: TypedLog
) : CoroutineScope {
    private val input = BroadcastChannel<Any>(Channel.BUFFERED)
    private var subscription: ReceiveChannel<Any> = Channel()
    private val actions = Channel<() -> Job>(Channel.BUFFERED)
    override val coroutineContext: CoroutineContext =
        SupervisorJob() + newSingleThreadContext(log.label)

    init {
        launch {
            connector.input.collect {
                log.d("Received $it")
                input.send(it)
            }
        }
    }

    fun invoke(block: ClientDsl.() -> Unit) {
        subscription = input.openSubscription()
        block()
    }

    fun output() = when {
        subscription.isClosedForReceive.not() -> subscription
        else -> input.openSubscription()
    }.consumeAsFlow()

    fun openSubscription() {
        if (subscription.isClosedForReceive) throw Exception()
        subscription = input.openSubscription()
    }

    fun send(vararg any: Any) = apply {
        actions.offer { launch { any.forEach { connector.output(it) } } }
    }

    suspend fun flush() {
        while (true) {
            actions.poll()
                ?.invoke()
                ?.join()
                ?: break
        }
    }

    suspend inline fun <reified T> waitFor(
        timeout: Long = 20_000,
        crossinline filter: T.() -> Boolean = { true }
    ): T = withTimeout(timeout) {
        flush()
        output().filterIsInstance<T>().first { it.filter() }
    }
}
