package cc.cryptopunks.crypton.smack.net.roster

import cc.cryptopunks.crypton.context.Roster
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.jivesoftware.smack.roster.Roster as SmackRoster

internal class RosterEvents(roster: SmackRoster) :
    Roster.Net.Events,
    Flow<Roster.Net.Event> by roster.rosterEventFlow() {

    init {
        roster.subscriptionMode = SmackRoster.SubscriptionMode.reject_all
    }
}

private fun SmackRoster.rosterEventFlow(): Flow<Roster.Net.Event> = callbackFlow {
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