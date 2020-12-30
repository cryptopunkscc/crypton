package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.context.Roster
import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.context.rosterNet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flattenConcat
import kotlinx.coroutines.flow.flowOf

internal fun SessionScope.presenceChangedFlow(
    rosterNet: Roster.Net = this.rosterNet
): Flow<Roster.PresenceChanged> =
    flowOf(
        rosterNet.getCachedPresences().map { presence ->
            Roster.PresenceChanged(
                presence = presence
            )
        }.asFlow(),
        rosterNet.rosterEvents.filterIsInstance<Roster.PresenceChanged>()
    ).flattenConcat().distinctUntilChanged()
