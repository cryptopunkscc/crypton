package cc.cryptopunks.crypton.agent.fake

import cc.cryptopunks.crypton.agent.Broadcast
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.asFlow

fun fakeBroadcast() = Broadcast(
    input = broadcastChannel.asFlow(),
    output = { broadcastChannel.send(this) }
)

private val broadcastChannel = BroadcastChannel<ByteArray>(Channel.BUFFERED)
