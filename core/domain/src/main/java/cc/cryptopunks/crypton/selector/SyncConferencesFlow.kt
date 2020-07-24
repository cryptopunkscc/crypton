package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.context.SessionScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun SessionScope.syncConferencesFlow(): Flow<Exec.SyncConferences> =
    accountAuthenticatedFlow().map { Exec.SyncConferences }
