package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.util.log
import javax.inject.Inject

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