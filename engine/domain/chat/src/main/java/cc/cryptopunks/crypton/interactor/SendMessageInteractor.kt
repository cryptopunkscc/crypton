package cc.cryptopunks.crypton.interactor

import cc.cryptopunks.crypton.entity.Chat
import cc.cryptopunks.crypton.entity.Message
import cc.cryptopunks.crypton.entity.Session
import kotlinx.coroutines.Job
import javax.inject.Inject

class SendMessageInteractor @Inject constructor(
    scope: Session.Scope,
    chat: Chat,
    sendMessage: Message.Net.Send
) : (String) -> Job by { message ->
    scope.launch {
        sendMessage(
            chat.address,
            message
        )
    }
}