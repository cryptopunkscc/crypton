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
            conversation: Chat,
            cause: Throwable
        ) : this(listOf(conversation), cause)
    }

    interface Repo {
        fun dataSourceFactory(): DataSource.Factory<Int, Chat>
        suspend fun insert(chat: Chat): Long
        fun delete(chat: Chat)
        fun deleteAll()
    }

    companion object {
        val Empty = Chat()
    }
}