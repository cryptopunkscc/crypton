package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.context.Connectable
import cc.cryptopunks.crypton.context.Connector
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ConnectableBuffer(
    override val coroutineContext: CoroutineContext
) : Connectable {

    private val log = typedLog()

    private val inputProxy = BroadcastChannel<Any>(Channel.BUFFERED)

    private val outputProxy = BroadcastChannel<Any>(Channel.BUFFERED)

    private var subscription: ReceiveChannel<Any> = inputProxy.openSubscription()

    private val connector = object : Connector {

        override val input: Flow<Any>
            get() = subscription
                .consumeAsFlow()

        override val output: suspend (Any) -> Unit get() = {
            outputProxy.send(it)
        }
    }

    var service: Connectable? = null
        set(value) {
            if (value == null) {
                subscription = inputProxy.openSubscription()
            }
            field?.cancel()
            field = value?.apply {
                connector.connect()
            }
        }

    override fun Connector.connect(): Job = launch {
        launch {
            log.d("bind")
            input.collect {
                inputProxy.send(it)
            }
        }
        launch {
            outputProxy.asFlow().collect(output)
        }
    }
}