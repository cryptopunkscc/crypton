package cc.cryptopunks.crypton.interactor

import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.Session
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal class SendMessageInteractor(
    scope: Session.Scope,
    chat: Chat,
    messageNet: Message.Net
) : (String) -> Job by { message ->
    scope.launch {
        messageNet.sendMessage(
            chat.address,
            message
        )
    }
}
