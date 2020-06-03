package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.context.Network
import cc.cryptopunks.crypton.util.ext.bufferedThrottle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class NetworkStatusFlowSelector(
    private val networkSys: Network.Sys
): () -> Flow<Network.Status> by {
    networkSys.statusFlow().bufferedThrottle(200).map { it.last() }
}
