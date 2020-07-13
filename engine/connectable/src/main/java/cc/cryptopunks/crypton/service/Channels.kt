package cc.cryptopunks.crypton.service

import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel

data class Channels(
    val actor: BroadcastChannel<Any> = BroadcastChannel(
        Channel.BUFFERED
    ),
    val service: BroadcastChannel<Any> = BroadcastChannel(
        Channel.BUFFERED
    )
) {
    fun cancel() {
        actor.cancel()
        service.cancel()
    }
}
