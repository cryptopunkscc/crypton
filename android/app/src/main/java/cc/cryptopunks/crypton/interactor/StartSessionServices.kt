package cc.cryptopunks.crypton.interactor

import cc.cryptopunks.crypton.entity.Session
import cc.cryptopunks.crypton.session.SessionServices
import javax.inject.Inject

class StartSessionServices @Inject constructor(
    private val createSessionServices: SessionServices.Factory
) : Session.StartServices {

    override fun invoke(session: Session): Unit = createSessionServices(session).run {
        messageService()
//        presenceService() // TODO: Presence
    }
}