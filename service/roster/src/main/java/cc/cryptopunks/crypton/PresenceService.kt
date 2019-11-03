package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.entity.RosterEvent
import cc.cryptopunks.crypton.manager.PresenceManager
import cc.cryptopunks.crypton.service.Service
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import javax.inject.Inject

class PresenceService @Inject constructor(
    rosterBroadcast: RosterEvent.Net.Broadcast,
    presenceManager: PresenceManager,
    scope: Service.Scope
) : () -> Job by {
    scope.launch {
        rosterBroadcast
            .filter { it.presence != null && it.resource != null }
            .collect { event ->
                presenceManager[event.resource!!.address] = event.presence!!
            }
    }
}