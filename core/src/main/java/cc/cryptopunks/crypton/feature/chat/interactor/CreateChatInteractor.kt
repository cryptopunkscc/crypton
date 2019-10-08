package cc.cryptopunks.crypton.feature.chat.interactor

import cc.cryptopunks.crypton.api.Client
import cc.cryptopunks.crypton.entity.Chat
import cc.cryptopunks.crypton.entity.User
import cc.cryptopunks.crypton.util.Scope
import kotlinx.coroutines.Deferred
import javax.inject.Inject

object CreateChat {

    class Interactor @Inject constructor(
        scope: Scope.UseCase,
        clientCache: Client.Cache,
        repo: Chat.Repo
    ) : (Data) -> Deferred<Chat> by { data ->
        scope.async {
            data.validate()

            val client = clientCache.values.first() // TODO
//            val user = client.getUser() TODO

            val chat = Chat(
                title = data.title,
                address = client.address
            )

            val chatId = repo.insert(chat)

            chat.copy(id = chatId)
        }
    }

    data class Data(
        val title: String,
        val users: List<User>
    ) {
        fun validate() {
            users.isNotEmpty() || throw Exception.EmptyUsers
        }

        data class Exception(
            override val message: String
        ) : kotlin.Exception() {

            companion object {
                val EmptyUsers = Exception("Users cannot be empty")
            }
        }
    }
}