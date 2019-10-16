package cc.cryptopunks.crypton.feature.chat.interactor

import cc.cryptopunks.crypton.api.Api
import cc.cryptopunks.crypton.entity.Address
import cc.cryptopunks.crypton.entity.Message
import cc.cryptopunks.crypton.entity.User
import kotlinx.coroutines.Job
import javax.inject.Inject

class SaveMessagesInteractor @Inject constructor(
    scope: Api.Scope,
    address: Address,
    messageRepo: Message.Repo,
    createChat: CreateChatInteractor
) : (List<Message>) -> Job by { messages ->
    scope.launch {
        messages.map { message ->
            message.copy(
                chatAddress = createChat(
                    CreateChatInteractor.Data(
                        title = message.chatAddress.id,
                        users = listOf(User(message.getParty(address).address))
                    )
                ).await().address
            )
        }.let {
            messageRepo.insert(it)
        }
    }
}