package cc.cryptopunks.crypton.domain.interactor

import cc.cryptopunks.crypton.api.Client
import cc.cryptopunks.crypton.entity.Account
import cc.cryptopunks.crypton.entity.Chat
import cc.cryptopunks.crypton.entity.ChatUser
import cc.cryptopunks.crypton.entity.User
import cc.cryptopunks.crypton.util.Scopes
import kotlinx.coroutines.Job
import javax.inject.Inject

object CreateChat {

    class Interactor @Inject constructor(
        scope: Scopes.UseCase,
        clientCache: Client.Cache,
        dao: ChatUser.Dao
    ) : (Data) -> Job by { data ->

        scope.launch {
            data.validate()

            val client = clientCache.values.first() // TODO
//            val user = client.getUser() TODO

            with(dao) {
                val chat = Chat(
                    title = data.title,
                    accountId = client.accountId
                )

                val chatId = insert(chat)

                val chatUserList = insertIfNeeded(data.users/* + client.user*/).map { userId ->
                    ChatUser(
                        chatId = chatId,
                        userId = userId
                    )
                }

                insert(chatUserList)

//                data.users.forEach(addContact)
            }
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