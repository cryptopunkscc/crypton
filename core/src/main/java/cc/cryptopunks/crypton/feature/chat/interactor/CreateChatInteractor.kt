package cc.cryptopunks.crypton.feature.chat.interactor

import cc.cryptopunks.crypton.api.Client
import cc.cryptopunks.crypton.entity.Chat
import cc.cryptopunks.crypton.entity.User
import cc.cryptopunks.crypton.util.Scopes
import kotlinx.coroutines.Job
import javax.inject.Inject

object CreateChat {

    class Interactor @Inject constructor(
        scope: Scopes.UseCase,
        clientCache: Client.Cache,
        repo: Chat.Repo
    ) : (Data) -> Job by { data ->

        scope.launch {
            data.validate()

            val client = clientCache.values.first() // TODO
//            val user = client.getUser() TODO

            val chat = Chat(
                title = data.title,
                address = client.address
            )

            val chatId = repo.insert(chat)
        }
    }

    data class Data(
        val title: String = "",
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