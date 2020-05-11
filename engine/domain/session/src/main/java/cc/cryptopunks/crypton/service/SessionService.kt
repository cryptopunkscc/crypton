package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.context.Session
import cc.cryptopunks.crypton.interactor.SessionEventHandler
import cc.cryptopunks.crypton.selector.ApiEventSelector
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.flow.collect

class SessionService(
    private val apiEventSelector: ApiEventSelector,
    private val sessionEventHandler: SessionEventHandler
) : Session.BackgroundService {
    private val log = typedLog()
    override suspend fun invoke(){
        log.d("Start")
        apiEventSelector().collect { sessionEventHandler(it) }
    }
}
