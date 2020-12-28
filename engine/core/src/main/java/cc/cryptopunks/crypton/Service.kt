package cc.cryptopunks.crypton

import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel

data class Service(
    val input: Channel<Action> = Channel(Channel.BUFFERED),
    val output: BroadcastChannel<Any> = BroadcastChannel(Channel.BUFFERED),
)
