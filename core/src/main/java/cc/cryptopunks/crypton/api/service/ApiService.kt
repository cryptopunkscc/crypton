package cc.cryptopunks.crypton.api.service

import cc.cryptopunks.crypton.api.Client
import cc.cryptopunks.crypton.core.Core
import cc.cryptopunks.crypton.feature.chat.service.LoadArchivedMessagesService
import cc.cryptopunks.crypton.feature.chat.service.MessageReceiverService
import cc.cryptopunks.crypton.util.log
import dagger.Component
import javax.inject.Inject

class ApiService @Inject constructor(
    loadArchivedMessagesService: LoadArchivedMessagesService,
    messageReceiverService: MessageReceiverService
) : () -> Unit by {
    ApiService::class.log("start")
    messageReceiverService()
    loadArchivedMessagesService()
    ApiService::class.log("stop")
} {

    @Component(dependencies = [Client::class, Core.Component::class])
    internal interface Factory : () -> ApiService
}