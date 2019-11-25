package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.entity.Session
import cc.cryptopunks.crypton.manager.SessionManager
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SessionEventSelector @Inject constructor(
    private val sessionManager: SessionManager
) : () -> Flow<Session.Event> by { sessionManager }