package cc.cryptopunks.crypton.api.service

import cc.cryptopunks.crypton.api.Client
import cc.cryptopunks.crypton.core.Core
import cc.cryptopunks.crypton.feature.chat.service.MessageReceiverService
import dagger.Component
import javax.inject.Inject

class ApiService @Inject constructor(
    val messageReceiverService: MessageReceiverService
) : () -> Unit by {
    messageReceiverService()
} {

    @Component(dependencies = [Client::class, Core.Component::class])
    internal interface Factory : () -> ApiService
}