package cc.cryptopunks.crypton.api.service

import cc.cryptopunks.crypton.api.Client
import cc.cryptopunks.crypton.core.Core
import cc.cryptopunks.crypton.service.LoadArchivedMessagesService
import cc.cryptopunks.crypton.service.MessageReceiverService
import cc.cryptopunks.crypton.service.ShowMessageNotificationService
import cc.cryptopunks.crypton.util.log
import dagger.Component
import javax.inject.Inject

class ApiService @Inject constructor(
    messageReceiverService: MessageReceiverService,
    loadArchivedMessagesService: LoadArchivedMessagesService,
    showMessageNotificationService: ShowMessageNotificationService
) : () -> Unit by {
    log<ApiService>("start")
    messageReceiverService()
    loadArchivedMessagesService()
    showMessageNotificationService()
    log<ApiService>("stop")
} {

    @Component(dependencies = [Client::class, Core.Component::class])
    internal interface Factory : () -> ApiService
}