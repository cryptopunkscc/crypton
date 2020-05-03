package cc.cryptopunks.crypton.module

import cc.cryptopunks.crypton.context.SessionCore
import cc.cryptopunks.crypton.interactor.SessionEventHandler
import cc.cryptopunks.crypton.selector.ApiEventSelector
import cc.cryptopunks.crypton.service.SessionService

class SessionDomainModule(
    sessionCore: SessionCore
) :
    SessionCore by sessionCore {

    val sessionService by lazy {
        SessionService(
            apiEventSelector = ApiEventSelector(session),
            sessionEventHandler = SessionEventHandler(session, networkSys)
        )
    }
}
