package cc.cryptopunks.crypton.emitter

import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.context.Network
import cc.cryptopunks.crypton.context.RootScope
import cc.cryptopunks.crypton.util.ext.bufferedThrottle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull

internal fun RootScope.sessionActionsFlow(): Flow<Exec.Session> =
    networkSys.statusFlow().bufferedThrottle(200).map { it.last() }
        .mapNotNull { status: Network.Status ->
            when (status) {
                is Network.Status.Available,
                is Network.Status.Changed -> Exec.Session.Action.Reconnect
                else -> Exec.Session.Action.Interrupt
            }
        }.map { Exec.Session(it) }
