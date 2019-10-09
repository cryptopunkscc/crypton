package cc.cryptopunks.crypton.entity

import androidx.paging.DataSource

data class Chat(
    val id: Long = 0,
    val title: String = "",
    val address: Address = Address.Empty,
    val users: List<User> = emptyList()
) {

    data class Exception(
        val conversations: List<Chat>,
        override val cause: Throwable
    ) : kotlin.Exception(cause) {
        constructor(
            chat: Chat,
            cause: Throwable
        ) : this(listOf(chat), cause)
    }

    interface Repo {
        suspend fun get(id: Long): Chat
        suspend fun insert(chat: Chat): Long
        suspend fun delete(chat: Chat)
        suspend fun deleteAll()
        fun dataSourceFactory(): DataSource.Factory<Int, Chat>
    }

    companion object {
        val Empty = Chat()
    }
}