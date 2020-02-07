package cc.cryptopunks.crypton.context

import androidx.paging.DataSource
import kotlinx.coroutines.flow.Flow

data class Chat(
    val title: String = "",
    val address: Address = Address.Empty,
    val account: Address = Address.Empty,
    val resource: Resource = Resource.Empty,
    val users: List<User> = emptyList()
) {

    interface Event : Api.Event
    data class Joined(val chat: Chat) : Event

    val isDirect get() = users.size == 2
    val accountUser get() = users.last()

    interface Net {
        val createChat: Create
        val multiUserChatFlow: MultiUserChatFlow
        val multiUserChatList: MultiUserChatList

        interface Create: (Chat) -> Chat
        interface MultiUserChatFlow: Flow<Chat>
        interface MultiUserChatList: () -> List<Chat>
        interface EventFlow: Flow<Event>
    }

    interface Repo {
        suspend fun get(address: Address): Chat
        suspend fun list(addresses: List<Address>): List<Chat>
        suspend fun insert(chat: Chat)
        suspend fun insertIfNeeded(chat: Chat)
        suspend fun delete(chat: Chat)
        suspend fun deleteAll()
        fun dataSourceFactory(): DataSource.Factory<Int, Chat>
    }

    companion object {
        val Empty = Chat()
    }
}