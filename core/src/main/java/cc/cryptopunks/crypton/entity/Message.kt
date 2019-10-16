package cc.cryptopunks.crypton.entity

import androidx.paging.DataSource
import kotlinx.coroutines.flow.Flow

typealias CryptonMessage = Message

data class Message(
    val id: String = "",
    val stanzaId: String = "",
    val text: String = "",
    val timestamp: Long = 0,
    val chatAddress: Address = Address.Empty,
    val from: Resource = Resource.Empty,
    val to: Resource = Resource.Empty
) {

    fun getParty(address: Address) = when (address) {
        from.address -> to
        to.address -> from
        else -> throw Exception("$address is not in party")
    }

    interface Api {
        val sendMessage: Send
        val messageBroadcast: Broadcast
        val readArchived: ReadArchived

        interface Send : (Address, String) -> Unit
        interface Broadcast : Flow<Message>
        interface ReadArchived : (ReadArchived.Query) -> Flow<List<Message>> {
            data class Query(
                val since: Long ? = null,
                val until: Long = System.currentTimeMillis()
            )
        }
    }

    interface Repo {
        suspend fun insertOrUpdate(message: Message)
        suspend fun insert(messages: List<Message>)
        suspend fun latest(): Message?
        fun flowLatest(chat: Chat): Flow<Message>
        fun dataSourceFactory(chat: Chat): DataSource.Factory<Int, Message>
    }

    class Exception(message: String? = null) : kotlin.Exception(message)

    companion object {
        val Empty = Message()
    }
}