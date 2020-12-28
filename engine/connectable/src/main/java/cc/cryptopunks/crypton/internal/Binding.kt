package cc.cryptopunks.crypton.internal

import cc.cryptopunks.crypton.Connector
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.SendChannel

internal data class Binding(
    val actorContext: Context,
    val serviceContext: Context
) {
    data class Context(
        val input: BroadcastChannel<Any>,
        val output: SendChannel<Any>,
        val connector: Connector,
        val job: Job? = null
    )
}
