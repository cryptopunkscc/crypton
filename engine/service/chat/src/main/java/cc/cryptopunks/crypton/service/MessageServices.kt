package cc.cryptopunks.crypton.service

interface MessageServices {
    val messageReceiverService: MessageReceiverService
    val loadArchivedMessagesService: LoadArchivedMessagesService
    val messageNotificationService: MessageNotificationService
}