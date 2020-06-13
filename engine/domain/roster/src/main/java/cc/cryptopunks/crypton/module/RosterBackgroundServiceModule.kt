package cc.cryptopunks.crypton.module

import cc.cryptopunks.crypton.context.SessionCore
import cc.cryptopunks.crypton.interactor.StorePresenceInteractor
import cc.cryptopunks.crypton.selector.PresenceChangedFlowSelector
import cc.cryptopunks.crypton.service.RosterBackgroundService

class RosterBackgroundServiceModule(
    sessionCore: SessionCore
) : SessionCore by sessionCore {
    val rosterBackgroundService by lazy {
        RosterBackgroundService(
            presenceChangedFlow = PresenceChangedFlowSelector(
                rosterNet = sessionCore,
                presenceNet = sessionCore
            ),
            storePresence = StorePresenceInteractor(
                store = userPresenceStore
            )
        )
    }
}
