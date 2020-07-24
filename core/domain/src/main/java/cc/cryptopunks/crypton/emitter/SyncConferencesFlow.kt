package cc.cryptopunks.crypton.emitter

import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.selector.accountAuthenticatedFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun SessionScope.syncConferencesFlow(): Flow<Exec.SyncConferences> =
    accountAuthenticatedFlow().map { Exec.SyncConferences }
