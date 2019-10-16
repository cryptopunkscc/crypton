package cc.cryptopunks.crypton.entity

import androidx.paging.DataSource

data class Chat(
    val title: String = "",
    val address: Address = Address.Empty,
    val account: Address = Address.Empty,
    val resource: Resource = Resource.Empty,
    val users: List<User> = emptyList()
) {

    val isDirect get() = users.size == 2
    val accountUser get() = users.last()

    data class Exception(
        val conversations: List<Chat>,
        override val cause: Throwable
    ) : kotlin.Exception(cause) {
        constructor(
            chat: Chat,
            cause: Throwable
        ) : this(listOf(chat), cause)
    }

    interface Api {
        val createChat: Create
        interface Create: (Chat) -> Chat
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