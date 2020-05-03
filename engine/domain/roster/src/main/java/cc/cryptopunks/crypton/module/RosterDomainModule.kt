package cc.cryptopunks.crypton.module

import cc.cryptopunks.crypton.context.FeatureCore
import cc.cryptopunks.crypton.selector.LatestMessageFlowSelector
import cc.cryptopunks.crypton.selector.PresenceFlowSelector
import cc.cryptopunks.crypton.selector.RosterSelector
import cc.cryptopunks.crypton.service.RosterItemService
import cc.cryptopunks.crypton.service.RosterService

class RosterDomainModule(
    core: FeatureCore
) : FeatureCore by core {

    private val presenceSelector by lazy { PresenceFlowSelector(userPresenceStore) }

    private val latestMessageFlowSelector by lazy { LatestMessageFlowSelector(messageRepo) }

    val rosterService by lazy {
        RosterService(
            rosterFlow = RosterSelector(
                repo = chatRepo,
                mainExecutor = mainExecutor,
                ioExecutor = ioExecutor
            ),
            createRosterItem = RosterItemService.Factory(
                presenceSelector = presenceSelector,
                navigate = navigate,
                messageRepo = messageRepo,
                latestMessageFlow = latestMessageFlowSelector
            )
        )
    }
}

