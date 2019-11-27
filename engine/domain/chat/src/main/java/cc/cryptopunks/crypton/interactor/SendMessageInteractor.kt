package cc.cryptopunks.crypton.interactor

import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.Session
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