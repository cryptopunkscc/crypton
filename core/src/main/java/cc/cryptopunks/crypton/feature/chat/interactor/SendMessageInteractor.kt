package cc.cryptopunks.crypton.feature.chat.interactor

import cc.cryptopunks.crypton.api.Api
import cc.cryptopunks.crypton.entity.Chat
import cc.cryptopunks.crypton.entity.Message
import kotlinx.coroutines.Job
import javax.inject.Inject

class SendMessageInteractor @Inject constructor(
    scope: Api.Scope,
    chat: Chat,
    sendMessage: Message.Api.Send
) : (String) -> Job by { message ->
    scope.launch {
        sendMessage(
            chat.address,
            message
        )
    }
}