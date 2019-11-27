package cc.cryptopunks.crypton.interactor

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.Session
import cc.cryptopunks.crypton.context.User
import kotlinx.coroutines.Job
import javax.inject.Inject

class SaveMessagesInteractor @Inject constructor(
    scope: Session.Scope,
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
                        users = listOf(
                            User(
                                message.getParty(address).address
                            )
                        )
                    )
                ).await().address
            )
        }.let {
            messageRepo.insert(it)
        }
    }
}