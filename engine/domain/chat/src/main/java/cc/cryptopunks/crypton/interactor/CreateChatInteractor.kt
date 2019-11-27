package cc.cryptopunks.crypton.interactor

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Session
import cc.cryptopunks.crypton.context.User
import kotlinx.coroutines.Deferred
import javax.inject.Inject

class CreateChatInteractor @Inject constructor(
    scope: Session.Scope,
    repo: Chat.Repo,
    address: Address,
    createChat: Chat.Net.Create
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