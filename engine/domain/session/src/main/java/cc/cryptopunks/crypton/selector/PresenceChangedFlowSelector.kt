package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.context.*
import kotlinx.coroutines.flow.*

internal fun SessionScope.presenceChangedFlow(): Flow<Roster.Net.PresenceChanged> =
    flowOf(
        getCachedPresences().map { presence ->
            Roster.Net.PresenceChanged(
                presence = presence
            )
        }.asFlow(),
        rosterEvents.filterIsInstance()
    ).flattenConcat()
