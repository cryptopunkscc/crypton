package cc.cryptopunks.crypton.internal

import cc.cryptopunks.crypton.connector
import cc.cryptopunks.crypton.logging
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel

internal fun createBinding(
    actorChannel: BroadcastChannel<Any> = BroadcastChannel(Channel.BUFFERED),
    serviceChannel: BroadcastChannel<Any> = BroadcastChannel(Channel.BUFFERED)
) = Binding(
    actorContext = context(
        input = actorChannel,
        output = serviceChannel
    ),
    serviceContext = context(
        input = serviceChannel,
        output = actorChannel
    )
)

private fun context(
    input: BroadcastChannel<Any>,
    output: BroadcastChannel<Any>
) = Binding.Context(
    input = input,
    output = output,
    connector = connector(
        inputChannel = input.openSubscription(),
        outputChannel = output
    ).logging()
)
