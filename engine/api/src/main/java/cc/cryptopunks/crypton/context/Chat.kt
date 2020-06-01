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

        data class MessagesRead(val messages: List<Message>) : In

        data class SendMessage(val text: String) : In

        sealed class Option : In {
            abstract val message: Message

            data class Copy(override val message: Message) : Option()
        }

        object Subscribe {
            object PagedMessages : In
            data class ListMessages(
                val from: Long = 0,
                val to: Long = System.currentTimeMillis()
            ) : In
            object LastMessage
        }

        object Get {
            object ListMessages : In
        }

        data class CreateChat(val address: Address) : In

        data class MessageText(val text: CharSequence?) : Out

        data class PagedMessages(val account: Address, val list: PagedList<Message>) : Out

        data class Messages(val account: Address, val list: List<Message>)

    }


    interface Net {
        fun createChat(chat: Chat): Chat

        interface Event : Api.Event
        data class Joined(val chat: Chat) : Event

        interface Create : (Chat) -> Chat
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
