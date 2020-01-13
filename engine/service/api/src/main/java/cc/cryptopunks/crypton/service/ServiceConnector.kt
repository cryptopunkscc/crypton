package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.context.Service
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.asFlow

internal class ServiceConnector : Service.Connector {
    private val channel = BroadcastChannel<Any>(Channel.CONFLATED)
    override val input get() = channel.asFlow()
    override val output = channel::send
}