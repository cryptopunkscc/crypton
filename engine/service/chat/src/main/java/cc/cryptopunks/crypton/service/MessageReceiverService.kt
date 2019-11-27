package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.annotation.SessionScope
import cc.cryptopunks.crypton.entity.Message
import cc.cryptopunks.crypton.entity.Session
import cc.cryptopunks.crypton.interactor.SaveMessagesInteractor
import cc.cryptopunks.crypton.util.ext.invokeOnClose
import cc.cryptopunks.crypton.util.log
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@SessionScope
class MessageReceiverService @Inject constructor(
    scope: Session.Scope,
    messageBroadcast: Message.Net.Broadcast,
    saveMessages: SaveMessagesInteractor
) : () -> Job by {
    scope.launch {
        log<MessageReceiverService>("start")
        invokeOnClose { log<MessageReceiverService>("stop") }
        messageBroadcast.collect { message ->
            saveMessages(listOf(message))
        }
    }
}