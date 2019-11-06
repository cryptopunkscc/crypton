package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.entity.Account
import cc.cryptopunks.crypton.entity.Session
import cc.cryptopunks.crypton.manager.SessionManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SessionsSelector @Inject constructor(
    private val sessionManager: SessionManager
) : (Account.Status) -> Flow<Session> by {
    sessionManager
        .filter { it.status == Account.Status.Connected }
        .map { it.session }
}