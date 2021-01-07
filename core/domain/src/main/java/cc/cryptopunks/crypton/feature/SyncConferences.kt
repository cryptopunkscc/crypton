package cc.cryptopunks.crypton.feature

import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.context.SessionScopeTag
import cc.cryptopunks.crypton.create.emitter
import cc.cryptopunks.crypton.create.handler
import cc.cryptopunks.crypton.feature
import cc.cryptopunks.crypton.interactor.syncConferencesWithRetry
import cc.cryptopunks.crypton.selector.accountAuthenticatedFlow
import kotlinx.coroutines.flow.map

internal fun syncConferences() = feature(

    emitter = emitter(SessionScopeTag) {
        accountAuthenticatedFlow().map { Exec.SyncConferences }
    },

    handler = handler { out, _: Exec.SyncConferences ->
        syncConferencesWithRetry(out)
    }
)

