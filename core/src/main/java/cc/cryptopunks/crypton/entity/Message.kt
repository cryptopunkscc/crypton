package cc.cryptopunks.crypton.entity

import androidx.paging.DataSource
import cc.cryptopunks.crypton.util.RxPublisher
import kotlinx.coroutines.flow.Flow

typealias ApiMessage = Message

data class Message(
    val id: String = "",
    val stanzaId: String = "",
    val text: String = "",
    val timestamp: Long = 0,
    val from: Resource = Resource.Empty,
    val to: Resource = Resource.Empty
) {

    interface Api {
        val sendMessage: Send
        val messagePublisher: Publisher

        interface Send : (Address, String) -> Unit
        interface Publisher : RxPublisher<Message>
    }

    interface Repo {
        fun insertOrUpdate(chatId: Long, vararg message: Message)
        fun flowLatest(chatId: Long): Flow<Message>
        fun dataSourceFactory(chat: Chat): DataSource.Factory<Int, Message>
    }

    companion object {
        val Empty = Message()
    }
}