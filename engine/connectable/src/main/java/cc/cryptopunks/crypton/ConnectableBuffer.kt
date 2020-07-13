package cc.cryptopunks.crypton

import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ConnectableBuffer(
    override val coroutineContext: CoroutineContext
) : Actor {

    private val inputProxy = BroadcastChannel<Any>(Channel.BUFFERED)

    private val outputProxy = BroadcastChannel<Any>(Channel.BUFFERED)

    private var subscription: ReceiveChannel<Any> = inputProxy.openSubscription()

    var service: Connectable? = null
        set(value) {
            if (value == null) {
                subscription = inputProxy.openSubscription()
            }
            field?.cancel()
            field = value?.apply {
                Connector(
                    input = subscription.consumeAsFlow(),
                    output = { outputProxy.send(it) }
                ).connect()
            }
        }

    override fun Connector.connect(): Job = launch {
        launch {
            input.collect {
                inputProxy.send(it)
            }
        }
        launch {
            outputProxy.asFlow().collect(output)
        }
    }
}
