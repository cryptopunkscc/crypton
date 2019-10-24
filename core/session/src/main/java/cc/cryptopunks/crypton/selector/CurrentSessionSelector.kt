package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.entity.Session
import cc.cryptopunks.crypton.manager.SessionManager
import javax.inject.Inject

class CurrentSessionSelector @Inject constructor(
    private val sessionManager: SessionManager
) : () -> Session? by {
    sessionManager.getCurrent()
}