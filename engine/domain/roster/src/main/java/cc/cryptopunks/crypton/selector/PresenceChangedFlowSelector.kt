package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.context.*
import kotlinx.coroutines.flow.*

internal class PresenceChangedFlowSelector(
    private val userPresenceNet: UserPresence.Net,
    private val rosterNet: Roster.Net
) : () -> Flow<Roster.Net.PresenceChanged> {
    override fun invoke(): Flow<Roster.Net.PresenceChanged> =
        flowOf(
            userPresenceNet.getCached().map {
                Roster.Net.PresenceChanged(
                    resource = Resource(
                        it.address,
                        "unknown"
                    ), // TODO
                    presence = it.presence
                )
            }.asFlow(),
            rosterNet.rosterEvents.filterIsInstance()
        ).flattenConcat()
}
