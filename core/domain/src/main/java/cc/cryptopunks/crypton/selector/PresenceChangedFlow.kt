package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.context.Roster
import cc.cryptopunks.crypton.context.SessionScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flattenConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach

internal fun SessionScope.presenceChangedFlow(): Flow<Roster.PresenceChanged> =
    flowOf(
        getCachedPresences().map { presence ->
            Roster.PresenceChanged(
                presence = presence
            )
        }.asFlow(),
        rosterEvents.filterIsInstance<Roster.PresenceChanged>()
    ).flattenConcat().distinctUntilChanged().onEach {
        println("Presence changed $address $it")
    }
