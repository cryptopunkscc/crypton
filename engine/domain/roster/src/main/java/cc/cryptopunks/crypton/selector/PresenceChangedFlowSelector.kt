package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.context.*
import kotlinx.coroutines.flow.*

internal class PresenceChangedFlowSelector(
    private val presenceNet: Presence.Net,
    private val rosterNet: Roster.Net
) : () -> Flow<Roster.Net.PresenceChanged> {
    override fun invoke(): Flow<Roster.Net.PresenceChanged> =
        flowOf(
            presenceNet.getCached().map { presence ->
                Roster.Net.PresenceChanged(
                    presence = presence
                )
            }.asFlow(),
            rosterNet.rosterEvents.filterIsInstance()
        ).flattenConcat()
}
