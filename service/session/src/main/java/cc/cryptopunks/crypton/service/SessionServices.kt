package cc.cryptopunks.crypton.service

//import cc.cryptopunks.crypton.PresenceService

interface SessionServices {

    val messageService: MessageService
//    val presenceService: PresenceService // TODO: Presence
}


fun SessionServices.start() {
    messageService()
}