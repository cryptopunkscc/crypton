package cc.cryptopunks.crypton.mux.channel

import cc.cryptopunks.crypton.mux.CreateChannel
import cc.cryptopunks.crypton.mux.GetChannel
import kotlinx.coroutines.channels.Channel

class MapChannels(
    val channels: MutableMap<Short, Channel<ByteArray>?> = mutableMapOf(),
    val void: Channel<ByteArray> = Channel(),
    val ids: IdsQueue = IdsQueue(),
)

fun MapChannels.getter(): GetChannel =
    { id -> channels[id] ?: void }

fun MapChannels.factory(): CreateChannel = {
    val index = ids.next()
    val channel = Channel<ByteArray>()
    channel.invokeOnClose {
        ids += index
        channels -= index
    }
    channels[index] = channel
    index to channel
}

