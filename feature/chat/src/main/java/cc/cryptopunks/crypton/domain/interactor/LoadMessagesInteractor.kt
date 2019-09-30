package cc.cryptopunks.crypton.domain.interactor

import cc.cryptopunks.crypton.entity.Account
import cc.cryptopunks.crypton.entity.Chat
import cc.cryptopunks.crypton.entity.Message
import cc.cryptopunks.crypton.util.Scopes
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class LoadMessagesInteractor @Inject constructor(
    messageRepo: Message.Repo,
    chatRepo: Chat.Repo,
    accountRepo: Account.Repo,
    scope: Scopes.UseCase
) : () -> Job by {
    //TODO: replace mock witch integration
    scope.launch {
        accountRepo.list().firstOrNull()?.run {
            chatRepo.deleteAll()
            (1L..200).asFlow().collect {
                chatRepo.insert(
                    Chat(
                        id = it,
                        address = address,
                        title = "Chat $it"
                    )
                )

                messageRepo.insertOrUpdate(
                    it,
                    Message(
                        id = it.toString(),
                        text = "message $it"
                    )
                )
                delay(50)
            }
        }
    }
}