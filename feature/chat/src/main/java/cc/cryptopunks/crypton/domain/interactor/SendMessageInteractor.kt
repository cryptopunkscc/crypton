package cc.cryptopunks.crypton.domain.interactor

import cc.cryptopunks.crypton.entity.Message
import cc.cryptopunks.crypton.entity.RemoteId
import cc.cryptopunks.crypton.util.Scopes
import kotlinx.coroutines.Job
import javax.inject.Inject

class SendMessageInteractor @Inject constructor(
    scope: Scopes.UseCase,
    remoteId: RemoteId,
    sendMessage: Message.Api.Send
) : (String) -> Job by { message ->
    scope.launch {
        sendMessage(
            remoteId,
            message
        )
    }
}