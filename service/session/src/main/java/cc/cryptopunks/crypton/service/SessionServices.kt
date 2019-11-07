package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.PresenceService


interface SessionServices {
    val messageService: MessageService
    val presenceService: PresenceService
}

fun SessionServices.start() {
    messageService()
    presenceService()
}