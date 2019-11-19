package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.PresenceService


interface SessionServices {
    val sessionErrorService: SessionErrorService
    val netEventService: NetEventService
    val messageService: MessageService
    val presenceService: PresenceService
}

fun SessionServices.start() {
    sessionErrorService()
    netEventService()
    messageService()
    presenceService()
}