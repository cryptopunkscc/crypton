package cc.cryptopunks.crypton.feature.chat.interactor

import cc.cryptopunks.crypton.api.Api
import cc.cryptopunks.crypton.entity.Account
import cc.cryptopunks.crypton.entity.Chat
import cc.cryptopunks.crypton.entity.Message
import kotlinx.coroutines.Job
import javax.inject.Inject

class LoadMessagesInteractor @Inject constructor(
    messageRepo: Message.Repo,
    chatRepo: Chat.Repo,
    accountRepo: Account.Repo,
    scope: Api.Scope
) : () -> Job by {
    //TODO: replace mock witch integration
    scope.launch {
//        accountRepo.list().firstOrNull()?.run {
//            chatRepo.deleteAll()
//            (1L..200).asFlow().collect {
//                chatRepo.insert(
//                    Chat(
//                        id = it,
//                        address = address,
//                        title = "Chat $it"
//                    )
//                )
//
//                messageRepo.insertOrUpdate(
//                    it,
//                    Message(
//                        id = it.toString(),
//                        text = "message $it"
//                    )
//                )
//                delay(50)
//            }
//        }
    }
}