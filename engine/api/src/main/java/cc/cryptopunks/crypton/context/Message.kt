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
    val readAt: Long = 0
) {

    val isUnread get() = readAt == 0L && status == Status.Received

    val author: String get() = from.address.local

    enum class Status {
        None,
        Sending,
        Error,
        Sent,
        Received,
        Read,
        Queued
    }

    fun getParty(address: Address) = when (address) {
        from.address -> to
        to.address -> from
        else -> throw Exception("$address is not in party")
    }

    interface Consumer {
        fun canConsume(message: Message): Boolean
    }

    sealed class Event : Api.Event {
        abstract val message: Message

        data class Queued(override val message: Message) : Event()
        data class Sending(override val message: Message) : Event()
        data class Sent(override val message: Message) : Event()
        data class Received(override val message: Message) : Event()
//        data class Error(override val message: Message, val cause: Throwable) : Event()
    }

    interface Net {
        val sendMessage: Send
        val messageBroadcast: Broadcast
        val readArchived: ReadArchived

        interface Send : SuspendFun2<Address, String, Unit>
        interface Broadcast : Flow<Event>
        interface ReadArchived : (ReadArchived.Query) -> Flow<List<Message>> {
            data class Query(
                val since: Long? = null,
                val afterUid: String? = null,
                val until: Long = System.currentTimeMillis()
            )
        }
    }

    interface Repo {
        suspend fun insertOrUpdate(message: Message)
        suspend fun insertOrUpdate(messages: List<Message>)
        suspend fun latest(): Message?
        suspend fun get(id: String): Message?
        suspend fun delete(message: Message)
        suspend fun listUnread(): List<Message>
        fun flowLatest(chat: Chat): Flow<Message>
        fun dataSourceFactory(chat: Chat): DataSource.Factory<Int, Message>
        fun unreadListFlow(): Flow<List<Message>>
        fun unreadCountFlow(chat: Chat) : Flow<Int>
        suspend fun notifyUnread()
    }

    object Notification {
        const val channelId = "Messages"
    }

    interface Notify {
        val notifyUnreadMessages: Unread

        interface Unread : (List<Message>) -> Unit {
            fun cancel(messages: List<Message>)
        }
    }

    interface Sys : Notify

    class Exception(message: String? = null) : kotlin.Exception(message)

    companion object {
        val Empty = Message()
    }
}

fun Any?.canConsume(message: Message): Boolean =
    (this is Message.Consumer) && canConsume(message)