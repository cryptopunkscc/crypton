package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.annotation.SessionScope
import cc.cryptopunks.crypton.context.Roster
import cc.cryptopunks.crypton.context.Service
import cc.cryptopunks.crypton.context.UserPresence
import cc.cryptopunks.crypton.manager.PresenceManager
import cc.cryptopunks.crypton.util.JobManager
import cc.cryptopunks.crypton.util.ext.invokeOnClose
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterIsInstance
import javax.inject.Inject

@SessionScope
class PresenceService @Inject constructor(
    rosterEvents: Roster.Net.Events,
    getCachedPresences: UserPresence.Net.GetCached,
    presenceManager: PresenceManager,
    scope: Service.Scope
) : () -> Unit {

    private val log = typedLog()

    private val jobManager = JobManager<Unit>(scope) {
        scope.run {
            launch {
                log.d("start loading cached presences")
                getCachedPresences().forEach { (address, presence) ->
                    presenceManager.send(address, presence)
                }
            }
            launch {
                log.d("start collecting")
                invokeOnClose { log.d("stop") }
                rosterEvents
                    .filterIsInstance<Roster.Net.PresenceChanged>()
                    .collect { event ->
                        presenceManager.send(event.resource.address, event.presence)
                    }
            }
        }
    }

    override fun invoke() {
        jobManager.run {
            cancel(Unit)
            invoke(Unit)
        }
    }
}