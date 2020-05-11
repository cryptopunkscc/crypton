package cc.cryptopunks.crypton.context

import androidx.paging.DataSource
import cc.cryptopunks.crypton.util.SuspendFun2
import kotlinx.coroutines.flow.Flow

typealias CryptonMessage = Message

data class Message(
    val id: String = "",
    val stanzaId: String = "",
    val text: String = "",
    val timestamp: Long = 0,
    val chatAddress: Address = Address.Empty,
    val from: Resource = Resource.Empty,
    val to: Resource = Resource.Empty,
    val status: Status = Status.None,
    val notifiedAt: Long = 0,
    val readAt: Long = 0
) {
    enum class Status {
        None,
        Sending,
        Error,
        Sent,
        Received,
        Read,
        Queued
    }

    interface Net {
        val sendMessage: Send
        val messageEvents: Events
        val readArchived: ReadArchived

        interface Send : SuspendFun2<Address, String, Unit>
        interface Events : Flow<Event>
        interface ReadArchived : (ReadArchived.Query) -> Flow<List<Message>> {
            data class Query(
                val since: Long? = null,
                val afterUid: String? = null,
                val until: Long = System.currentTimeMillis(),
                val chat: Chat? = null
            )
        }

        sealed class Event : Api.Event {
            abstract val message: Message

            data class Queued(override val message: Message) : Event()
            data class Sending(override val message: Message) : Event()
            data class Sent(override val message: Message) : Event()
            data class Received(override val message: Message) : Event()
        }
    }

    interface Repo {
        suspend fun insertOrUpdate(message: Message)
        suspend fun insertOrUpdate(messages: List<Message>)
        suspend fun latest(): Message?
        suspend fun get(id: String): Message?
        suspend fun delete(message: Message)
        suspend fun listUnread(): List<Message>
        fun flowLatest(chatAddress: Address): Flow<Message>
        fun dataSourceFactory(chatAddress: Address): DataSource.Factory<Int, Message>
        fun unreadListFlow(): Flow<List<Message>>
        fun unreadCountFlow(chatAddress: Address) : Flow<Int>
        suspend fun notifyUnread()
    }

    interface Consumer {
        fun canConsume(message: Message): Boolean
    }

    object Notification {
        const val channelId = "Messages"
    }

    class Exception(message: String? = null) : kotlin.Exception(message)

    companion object {
        val Empty = Message()
    }


    fun getParty(address: Address) = when (address) {
        from.address -> to
        to.address -> from
        else -> throw Exception("$address is not in party")
    }

    val isUnread get() = readAt == 0L && status == Status.Received

    val author: String get() = from.address.local
}

fun Any?.canConsume(message: Message): Boolean =
    (this is Message.Consumer) && canConsume(message)
