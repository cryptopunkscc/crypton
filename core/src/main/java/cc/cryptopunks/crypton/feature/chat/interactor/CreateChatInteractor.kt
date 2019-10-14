package cc.cryptopunks.crypton.feature.chat.interactor

import cc.cryptopunks.crypton.api.Api
import cc.cryptopunks.crypton.entity.Address
import cc.cryptopunks.crypton.entity.Chat
import cc.cryptopunks.crypton.entity.User
import kotlinx.coroutines.Deferred
import javax.inject.Inject

class CreateChatInteractor @Inject constructor(
    scope: Api.Scope,
    repo: Chat.Repo,
    address: Address,
    createChat: Chat.Api.Create
) : (CreateChatInteractor.Data) -> Deferred<Chat> by { data ->
    scope.async {
        data.run {

            validate()
            Chat(
                title = title,
                users = users + User(address),
                account = address
            )
        }.run {

            if (!isDirect)
                createChat(this) else
                copy(address = data.users.first().address)

        }.also { chat ->

            repo.insertIfNeeded(chat)
        }
    }
} {

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