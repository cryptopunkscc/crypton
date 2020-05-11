package cc.cryptopunks.crypton.module

import cc.cryptopunks.crypton.context.AppCore
import cc.cryptopunks.crypton.selector.*
import cc.cryptopunks.crypton.service.RosterItemService
import cc.cryptopunks.crypton.service.RosterService
import cc.cryptopunks.crypton.service.RosterService2

class RosterDomainModule(
    appCore: AppCore
) : AppCore by appCore {

    val rosterService by lazy {
        // TODO
        val sessionCore = appCore.sessionCore()
        RosterService(
            rosterPagedListFlow = RosterPagedListSelector(
                mainExecutor = mainExecutor,
                ioExecutor = ioExecutor,
                repo = sessionCore.chatRepo
            ),
            createRosterItem = RosterItemService.Factory(
                presenceSelector = PresenceFlowSelector(userPresenceStore),
                navigate = navigate,
                messageRepo = sessionCore.messageRepo,
                latestMessageFlow = LatestMessageFlowSelector(sessionCore.messageRepo)
            )
        )
    }

    val rosterService2 by lazy {
        RosterService2(
            rosterListFlowSelector = RosterItemStateListFlowSelector(
                sessionStore = sessionStore,
                createRosterItemStateFlowSelector = RosterItemStateFlowSelector.Factory(
                    presenceFlow = PresenceFlowSelector(userPresenceStore)
                )
            )
        )
    }
}

