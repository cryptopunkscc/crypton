package cc.cryptopunks.crypton.feature.chat.service

import cc.cryptopunks.crypton.api.Api
import cc.cryptopunks.crypton.entity.Message
import cc.cryptopunks.crypton.feature.chat.interactor.SaveMessagesInteractor
import cc.cryptopunks.crypton.util.ext.invokeOnClose
import cc.cryptopunks.crypton.util.log
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import javax.inject.Inject

class MessageReceiverService @Inject constructor(
    scope: Api.Scope,
    messageBroadcast: Message.Api.Broadcast,
    saveMessages: SaveMessagesInteractor
) : () -> Job by {
    scope.plus(SupervisorJob()).launch {
        MessageReceiverService::class.log("start")
        invokeOnClose { MessageReceiverService::class.log("stop") }
        messageBroadcast.collect { message ->
            scope.launch {
                saveMessages(listOf(message)).join()
            }.join()
        }
    }
}