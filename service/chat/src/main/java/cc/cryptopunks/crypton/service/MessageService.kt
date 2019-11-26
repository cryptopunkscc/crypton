package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.annotation.SessionScope
import cc.cryptopunks.crypton.util.log
import javax.inject.Inject

@SessionScope
class MessageService @Inject constructor(
    messageReceiverService: MessageReceiverService,
    loadArchivedMessagesService: LoadArchivedMessagesService,
    messageNotificationService: MessageNotificationService
) : () -> Unit by {
    log<MessageService>("start")
    messageReceiverService()
    loadArchivedMessagesService()
    messageNotificationService()
    log<MessageService>("stop")
}

interface MessageServices {
    val messageReceiverService: MessageReceiverService
    val loadArchivedMessagesService: LoadArchivedMessagesService
    val messageNotificationService: MessageNotificationService
}