package cc.cryptopunks.crypton.context

import androidx.paging.DataSource
import androidx.paging.PagedList
import kotlinx.coroutines.flow.Flow

data class Chat(
    val title: String = "",
    val address: Address = Address.Empty,
    val account: Address = Address.Empty,
    val resource: Resource = Resource.Empty,
    val users: List<User> = emptyList()
) {
    val isDirect get() = users.size == 2
    val accountUser get() = users.last()


    companion object {
        val Empty = Chat()
    }


    interface Service : Connectable {

        object PopClipboard

        data class MessagesRead(val messages: List<Message>)

        data class SendMessage(val text: String)

        data class Copy(val message: Message)

        data class SubscribePagedMessages(override var enable: Boolean) : Subscription

        data class SubscribeLastMessage(override var enable: Boolean) : Subscription

        data class SubscribeListMessages(
            override var enable: Boolean = true,
            val from: Long = 0,
            val to: Long = System.currentTimeMillis()
        ) : Subscription

        data class GetMessages(val address: Address? = null)

        data class CreateChat(val address: Address)

        data class MessageText(val text: CharSequence?) : Out

        data class PagedMessages(val account: Address, val list: PagedList<Message>) : Out

        data class Messages(val account: Address, val list: List<Message>)

        data class CreateChatData(
            val title: String,
            val users: List<Address>
        ) {
            fun validate() = users.isNotEmpty() || throw Exception.EmptyUsers

            data class Exception(
                override val message: String
            ) : kotlin.Exception() {

                companion object {
                    val EmptyUsers = Exception("Users cannot be empty")
                }
            }
        }
    }


    interface Net {
        fun createChat(chat: Chat): Chat

        interface Event : Api.Event
        data class Joined(val chat: Chat) : Event

        interface EventFlow : Flow<Event>
    }


    interface Repo {
        suspend fun get(address: Address): Chat
        suspend fun list(addresses: List<Address>): List<Chat>
        suspend fun insert(chat: Chat)
        suspend fun insertIfNeeded(chat: Chat)
        suspend fun delete(chat: Chat)
        suspend fun deleteAll()
        fun dataSourceFactory(): DataSource.Factory<Int, Chat>
        fun flowList(): Flow<List<Chat>>
    }
}

suspend fun Session.createChat(data: Chat.Service.CreateChatData) =
    data.run {
        log.d("Chat $users creating")
        validate()
        Chat(
            title = title,
            users = users.map(::User) + User(address),
            account = address
        )
    }.run {
        if (!isDirect)
            createChat(this) else
            copy(address = users.first().address)
    }.also { chat ->
        chatRepo.insertIfNeeded(chat)
        log.d("Chat ${chat.address} with users ${chat.users} created")
    }
