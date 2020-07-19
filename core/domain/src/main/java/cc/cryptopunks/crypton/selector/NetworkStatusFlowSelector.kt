package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.context.AppScope
import cc.cryptopunks.crypton.context.Network
import cc.cryptopunks.crypton.util.ext.bufferedThrottle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal fun AppScope.networkStatusFlow(): Flow<Network.Status> =
    networkSys.statusFlow().bufferedThrottle(200).map { it.last() }

