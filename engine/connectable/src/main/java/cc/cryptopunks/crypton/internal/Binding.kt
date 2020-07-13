package cc.cryptopunks.crypton.internal

import cc.cryptopunks.crypton.Connector
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BroadcastChannel

internal data class Binding(
    val actorContext: Context,
    val serviceContext: Context
) {
    data class Context(
        val input: BroadcastChannel<Any>,
        val output: BroadcastChannel<Any>,
        val connector: Connector,
        val job: Job? = null
    )
}
