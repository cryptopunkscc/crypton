package cc.cryptopunks.crypton.session

import cc.cryptopunks.crypton.entity.Session
import cc.cryptopunks.crypton.service.MessageService
//import cc.cryptopunks.crypton.service.PresenceService

interface SessionServices {

    val messageService: MessageService
//    val presenceService: PresenceService // TODO: Presence

    interface Factory : (Session) -> SessionServices
}