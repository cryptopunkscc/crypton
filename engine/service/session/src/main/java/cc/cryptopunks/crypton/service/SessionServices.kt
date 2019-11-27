package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.PresenceService
import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.context.Net
import cc.cryptopunks.crypton.context.Session


interface SessionServices : MessageServices {
    val sessionErrorService: SessionErrorService
    val netEventService: NetEventService
    val messageService: MessageService
    val presenceService: PresenceService
}

fun SessionServices.process(sessionEvent: Session.Event) {
    when (sessionEvent.event) {
        is Session.Event.Created -> {
            sessionErrorService()
            netEventService()
            presenceService()
            messageReceiverService()
            messageNotificationService()
        }
        is Net.Event.Connected -> {

        }
        is Net.Event.OmemoInitialized -> {
            loadArchivedMessagesService()
        }
        is Account.Event.Authenticated -> {
//            loadArchivedMessagesService()
        }
    }
}