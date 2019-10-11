package cc.cryptopunks.crypton.smack.api.roster

import cc.cryptopunks.crypton.entity.RosterEvent
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.jivesoftware.smack.roster.Roster

internal class RosterEventPublisher(roster: Roster) :
    RosterEvent.Api.Broadcast,
    Flow<RosterEvent> by roster.rosterEventFlow() {

    init {
        roster.subscriptionMode = Roster.SubscriptionMode.reject_all
    }
}

private fun Roster.rosterEventFlow(): Flow<RosterEvent> = callbackFlow {
    val adapter = RosterFlowAdapter(channel)

    addRosterLoadedListener(adapter)
    addPresenceEventListener(adapter)
    addSubscribeListener(adapter)

    awaitClose {
        removeRosterLoadedListener(adapter)
        removePresenceEventListener(adapter)
        removeSubscribeListener(adapter)
    }
}