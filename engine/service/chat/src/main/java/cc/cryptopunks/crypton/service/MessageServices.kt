package cc.cryptopunks.crypton.service

interface MessageServices {
    val chatSyncService: ChatSyncService
    val messageReceiverService: MessageReceiverService
    val loadArchivedMessagesService: LoadArchivedMessagesService
    val messageNotificationService: MessageNotificationService
}