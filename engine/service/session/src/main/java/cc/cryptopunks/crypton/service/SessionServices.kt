package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.PresenceService
import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.context.Net
import cc.cryptopunks.crypton.context.Session


interface SessionServices : MessageServices {
    val sessionErrorService: SessionErrorService
    val netEventService: NetEventService
    val presenceService: PresenceService
}

operator fun SessionServices.invoke(sessionEvent: Session.Event) {
    when (sessionEvent.event) {
        is Session.Created -> {
            sessionErrorService()
            netEventService()
            presenceService()
            messageReceiverService()
            messageNotificationService()
        }
        is Net.Connected -> {
            // no-op
        }
        is Net.OmemoInitialized -> {
            loadArchivedMessagesService()
        }
        is Account.Authenticated -> {
            // no-op
        }
    }
}