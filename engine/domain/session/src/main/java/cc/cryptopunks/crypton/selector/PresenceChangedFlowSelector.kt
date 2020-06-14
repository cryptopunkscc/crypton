package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.context.*
import kotlinx.coroutines.flow.*

internal class PresenceChangedFlowSelector(
    private val session: Session
) : () -> Flow<Roster.Net.PresenceChanged> {
    override fun invoke(): Flow<Roster.Net.PresenceChanged> =
        flowOf(
            session.getCachedPresences().map { presence ->
                Roster.Net.PresenceChanged(
                    presence = presence
                )
            }.asFlow(),
            session.rosterEvents.filterIsInstance()
        ).flattenConcat()
}
