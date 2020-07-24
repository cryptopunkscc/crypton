package cc.cryptopunks.crypton.internal

import cc.cryptopunks.crypton.Connector
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.consumeAsFlow

internal fun createBinding(
    actorChannel: BroadcastChannel<Any> = BroadcastChannel(Channel.BUFFERED),
    serviceChannel: BroadcastChannel<Any> = BroadcastChannel(Channel.BUFFERED)
) = Binding(
    actorContext = connectableHolder(
        input = actorChannel,
        output = serviceChannel
    ),
    serviceContext = connectableHolder(
        input = serviceChannel,
        output = actorChannel
    )
)

private fun connectableHolder(
    input: BroadcastChannel<Any>,
    output: BroadcastChannel<Any>
) = Binding.Context(
    input = input,
    output = output,
    connector = createConnector(
        inputChannel = input.openSubscription(),
        outputChannel = output
    )
)

internal fun createConnector(
    inputChannel: ReceiveChannel<Any>,
    outputChannel: SendChannel<Any>
) = Connector(
    input = inputChannel.consumeAsFlow(),
    output = {
        try {
            println("sending from connector: $it")
            outputChannel.send(it)
            println("sent from connector: $it")
        } catch (e: Throwable) {
            println("Cannot send $it")
            e.printStackTrace()
        }
    }
)
