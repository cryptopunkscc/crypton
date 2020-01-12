package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.context.Service
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.asFlow

@FlowPreview
@ExperimentalCoroutinesApi
internal class TwoWayBinding(
    private val leftChannel: BroadcastChannel<Any> = BroadcastChannel(
        Channel.BUFFERED
    ),
    private val rightChannel: BroadcastChannel<Any> = BroadcastChannel(
        Channel.CONFLATED
    )
) {
    val left = object : Service.Binding {
        override val input get() = rightChannel.asFlow()
        override val output = leftChannel::send
    }

    val right = object : Service.Binding {
        override val input get() = leftChannel.asFlow()
        override val output = rightChannel::send
    }
}