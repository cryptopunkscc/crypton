package cc.cryptopunks.crypton.session

import cc.cryptopunks.crypton.entity.Session
import cc.cryptopunks.crypton.service.MessageService

interface SessionServices {

    val messageService: MessageService

    interface Factory : (Session) -> SessionServices
}