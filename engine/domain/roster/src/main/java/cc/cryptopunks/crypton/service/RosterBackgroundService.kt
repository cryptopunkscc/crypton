package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.context.Session
import cc.cryptopunks.crypton.context.UserPresence
import cc.cryptopunks.crypton.interactor.StoreUserPresenceInteractor
import cc.cryptopunks.crypton.selector.PresenceChangedFlowSelector
import kotlinx.coroutines.flow.collect

class RosterBackgroundService internal constructor(
    private val presenceChangedFlow: PresenceChangedFlowSelector,
    private val storeUserPresence: StoreUserPresenceInteractor
) : Session.BackgroundService {
    override suspend fun invoke() {
        presenceChangedFlow().collect { (resource, presence) ->
            storeUserPresence(
                UserPresence(
                    address = resource.address,
                    presence = presence
                )
            )
        }
    }
}
