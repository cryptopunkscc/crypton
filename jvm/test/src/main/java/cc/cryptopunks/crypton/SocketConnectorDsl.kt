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

class SocketConnectorDsl(
    private val connector: Connector,
    val log: TypedLog
) : CoroutineScope {
    private val input = BroadcastChannel<Any>(Channel.BUFFERED)
    private var sub: ReceiveChannel<Any>? = null
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

    fun output() = let {
        sub?.takeUnless { it.isClosedForReceive } ?: input.openSubscription()
    }.consumeAsFlow()

    fun openSubscription() {
        if (sub?.isClosedForReceive == true) throw Exception()
        sub = input.openSubscription()
    }

    fun invoke(block: SocketConnectorDsl.() -> Unit) {
        sub = input.openSubscription()
        block()
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
        timeout: Long = 10_000,
        crossinline filter: T.() -> Boolean = { true }
    ): T = withTimeout(timeout) {
        flush()
        output().filterIsInstance<T>().first { it.filter() }
    }
}
