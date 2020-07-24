package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.context.AppScope
import cc.cryptopunks.crypton.context.Main
import cc.cryptopunks.crypton.context.Network
import cc.cryptopunks.crypton.util.ext.bufferedThrottle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal fun AppScope.sessionCommandFlow(): Flow<Main.Command.Session> =
    networkSys.statusFlow().bufferedThrottle(200).map { it.last() }.map { status: Network.Status ->
        when (status) {
            is Network.Status.Available,
            is Network.Status.Changed -> Main.Command.Session.Action.Reconnect
            else -> Main.Command.Session.Action.Interrupt
        }
    }.map { Main.Command.Session(it) }

