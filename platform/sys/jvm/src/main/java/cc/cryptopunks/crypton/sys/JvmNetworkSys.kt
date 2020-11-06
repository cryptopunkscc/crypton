package cc.cryptopunks.crypton.sys

import cc.cryptopunks.crypton.context.Network
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class JvmNetworkSys : Network.Sys {

    override val status: Network.Status =
        Network.Status.Available
    override fun statusFlow(): Flow<Network.Status> =
        flowOf(status)
}
