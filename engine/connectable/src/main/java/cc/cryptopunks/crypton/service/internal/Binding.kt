package cc.cryptopunks.crypton.service.internal

import cc.cryptopunks.crypton.context.Connector
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
