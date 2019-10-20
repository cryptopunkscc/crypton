package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.net.Net
import cc.cryptopunks.crypton.entity.Chat
import cc.cryptopunks.crypton.entity.Message
import cc.cryptopunks.crypton.presentation.PresentationManager
import cc.cryptopunks.crypton.util.log
import dagger.Component
import javax.inject.Inject

class MessageService @Inject constructor(
    messageReceiverService: MessageReceiverService,
    loadArchivedMessagesService: LoadArchivedMessagesService,
    showMessageNotificationService: ShowMessageNotificationService
) : () -> Unit by {
    log<MessageService>("start")
    messageReceiverService()
    loadArchivedMessagesService()
    showMessageNotificationService()
    log<MessageService>("stop")
} {

    @Component(
        dependencies = [
            Net::class,
            Message.Net::class,
            Chat.Net::class,
            Chat.Repo::class,
            Message.Net::class,
            Message.Repo::class,
            Message.Sys::class,
            PresentationManager::class
        ]
    )
    interface Factory : () -> MessageService
}