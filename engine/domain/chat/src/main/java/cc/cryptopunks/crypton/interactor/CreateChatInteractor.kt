package cc.cryptopunks.crypton.interactor

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.User
import cc.cryptopunks.crypton.util.typedLog

internal class CreateChatInteractor(
    private val repo: Chat.Repo,
    private val address: Address,
    private val net: Chat.Net
) {
    private val log = typedLog()

    suspend operator fun invoke(data: Data) = data.run {
        log.d("Chat ${data.users} creating")
        validate()
        Chat(
            title = title,
            users = users + User(address),
            account = address
        )
    }.run {
        if (!isDirect)
            net.createChat(this) else
            copy(address = data.users.first().address)
    }.also { chat ->
        repo.insertIfNeeded(chat)
        log.d("Chat ${chat.address} with users ${chat.users} created")
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
