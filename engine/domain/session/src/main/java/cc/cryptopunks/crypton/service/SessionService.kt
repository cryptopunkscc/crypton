package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.context.Session
import cc.cryptopunks.crypton.interactor.SessionEventHandler
import cc.cryptopunks.crypton.selector.ApiEventSelector
import kotlinx.coroutines.flow.collect

class SessionService(
    private val apiEventSelector: ApiEventSelector,
    private val sessionEventHandler: SessionEventHandler
) : Session.BackgroundService {

    override suspend fun invoke(){
        apiEventSelector().collect { sessionEventHandler(it) }
    }
}
