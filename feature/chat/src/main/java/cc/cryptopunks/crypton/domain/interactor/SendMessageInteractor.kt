package cc.cryptopunks.crypton.domain.interactor

import cc.cryptopunks.crypton.entity.Message
import cc.cryptopunks.crypton.entity.Address
import cc.cryptopunks.crypton.util.Scopes
import kotlinx.coroutines.Job
import javax.inject.Inject

class SendMessageInteractor @Inject constructor(
    scope: Scopes.UseCase,
    address: Address,
    sendMessage: Message.Api.Send
) : (String) -> Job by { message ->
    scope.launch {
        sendMessage(
            address,
            message
        )
    }
}