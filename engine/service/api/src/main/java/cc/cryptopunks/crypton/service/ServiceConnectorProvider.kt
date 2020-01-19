package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.context.Service
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow

internal class ServiceConnectorProvider : Service.Connector {
    val log = typedLog()

    private val channel = BroadcastChannel<Any>(Channel.BUFFERED)

    override val input: Flow<Any> get() = throw NotImplementedError()

    override val output: suspend (Any) -> Unit = {
        log.d("out: $it")
        channel.send(it)
    }

    fun openSubscription() = object : Service.Connector {
        private val subscription = channel.openSubscription()

        override val input get() = subscription.consumeAsFlow()
        override val output get() = this@ServiceConnectorProvider.output
    }
}