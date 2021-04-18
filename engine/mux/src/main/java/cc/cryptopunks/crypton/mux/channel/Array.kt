package cc.cryptopunks.crypton.mux.channel

import cc.cryptopunks.crypton.mux.CreateChannel
import cc.cryptopunks.crypton.mux.GetChannel
import kotlinx.coroutines.channels.Channel

class ArrayChannels(
    val channels: Array<Channel<ByteArray>?> = Array(Short.MAX_VALUE.toInt()) { null },
    val void: Channel<ByteArray> = Channel(),
    val ids: IdsQueue = IdsQueue(),
)

fun ArrayChannels.getter(): GetChannel = { id ->
    channels[id.toInt()] ?: void
}

fun ArrayChannels.factory(): CreateChannel = {
    val id = ids.next()
    val index = id.toInt()
    val channel = Channel<ByteArray>()
    channel.invokeOnClose {
        ids += id
        channels[index] = null
    }
    channels[index] = channel
    index.toShort() to channel
}
