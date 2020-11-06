package cc.cryptopunks.crypton.emitter

import cc.cryptopunks.crypton.context.Net
import cc.cryptopunks.crypton.context.Network
import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.context.Subscribe
import cc.cryptopunks.crypton.util.ext.bufferedThrottle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

internal fun SessionScope.reconnectSessionFlow(): Flow<Subscribe.ReconnectSession> =
    flowOf(
        netEvents().filterIsInstance<Net.Disconnected>(),
        networkSys.statusFlow().bufferedThrottle(200).map { it.last() }.filter { status ->
            when (status) {
                is Network.Status.Available,
                is Network.Status.Changed -> true
                else -> false
            }
        }
    ).flattenMerge().map { Subscribe.ReconnectSession }
