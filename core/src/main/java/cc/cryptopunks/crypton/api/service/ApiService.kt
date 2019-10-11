package cc.cryptopunks.crypton.api.service

import cc.cryptopunks.crypton.api.Client
import cc.cryptopunks.crypton.feature.chat.service.MessageReceiverService
import cc.cryptopunks.crypton.service.Service
import dagger.Component
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class ApiService @Inject constructor(
    val messageReceiverService: MessageReceiverService
) : () -> Unit by {
    messageReceiverService()
} {

    @Component
    interface Factory : (Client) -> ApiService

    class Manager @Inject constructor(
        private val scope: Service.Scope,
        private val clientManager: Client.Manager,
        private val createClientService: Factory
    ) : () -> Job {

        override fun invoke() = scope.launch {
            clientManager.collect { createClientService(it) }
        }
    }
}