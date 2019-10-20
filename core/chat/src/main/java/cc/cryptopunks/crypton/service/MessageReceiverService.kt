package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.net.Net
import cc.cryptopunks.crypton.entity.Message
import cc.cryptopunks.crypton.interactor.SaveMessagesInteractor
import cc.cryptopunks.crypton.util.ext.invokeOnClose
import cc.cryptopunks.crypton.util.log
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class MessageReceiverService @Inject constructor(
    scope: Net.Scope,
    messageBroadcast: Message.Net.Broadcast,
    saveMessages: SaveMessagesInteractor
) : () -> Job by {
    scope.launch {
        MessageReceiverService::class.log("start")
        invokeOnClose { MessageReceiverService::class.log("stop") }
        messageBroadcast.collect { message ->
            saveMessages(listOf(message))
        }
    }
}