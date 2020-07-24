package cc.cryptopunks.crypton.context

import androidx.paging.DataSource
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import java.security.MessageDigest

typealias CryptonMessage = Message

fun messageStatus(status: String) = Message.Status.valueOf(status)

data class Message(
    val id: String = "",
    val stanzaId: String = "",
    val text: String = "",
    val timestamp: Long = 0,
    val chat: Address = Address.Empty,
    val from: Resource = Resource.Empty,
    val to: Resource = Resource.Empty,
    val status: Status = Status.None,
    val notifiedAt: Long = 0,
    val readAt: Long = 0,
    val encrypted: Boolean = true
) {

    companion object {
        val Empty = Message()
    }

    enum class Status {
        None,
        Queued,
        Sending,
        Sent,
        Error,
        Received,
        Read,
        Info,
        State
    }

    enum class State {
        /**
         * User is actively participating in the chat session.
         */
        active,

        /**
         * User is composing a message.
         */
        composing,

        /**
         * User had been composing but now has stopped.
         */
        paused,

        /**
         * User has not been actively participating in the chat session.
         */
        inactive,

        /**
         * User has effectively ended their participation in the chat session.
         */
        gone
    }

    data class Incoming(val message: Message) : Api.Event

    interface Net {
        suspend fun sendMessage(message: Message): Job
        fun incomingMessages(): Flow<Incoming>
        fun readArchived(query: ReadQuery): Flow<List<Message>>

        data class ReadQuery(
            val since: Long? = null,
            val afterUid: String? = null,
            val until: Long = System.currentTimeMillis(),
            val chat: Chat? = null
        )
    }

    interface Repo {
        suspend fun insertOrUpdate(message: Message)
        suspend fun insertOrUpdate(messages: List<Message>)
        suspend fun latest(): Message?
        suspend fun latest(chat: Address): Message?
        suspend fun get(id: String): Message?
        suspend fun delete(id: String)
        suspend fun delete(message: Message)
        suspend fun delete(message: List<Message>)
        suspend fun listUnread(): List<Message>
        suspend fun list(range: LongRange = 0..System.currentTimeMillis()): List<Message>
        suspend fun list(chat: Address, status: Status): List<Message>
        fun flowLatest(chatAddress: Address? = null): Flow<Message>
        fun dataSourceFactory(chatAddress: Address): DataSource.Factory<Int, Message>
        suspend fun queuedList(): List<Message>
        fun unreadListFlow(): Flow<List<Message>>
        fun queuedListFlow(): Flow<List<Message>>
        fun unreadCountFlow(chatAddress: Address): Flow<Int>
        suspend fun notifyUnread()
    }

    interface Consumer {
        fun canConsume(message: Message): Boolean
    }

    object Notification {
        const val channelId = "Messages"
    }

    class Exception(message: String? = null) : kotlin.Exception(message)

    val isUnread get() = readAt == 0L && status == Status.Received

    val author: String
        get() = when {
            !chat.isConference -> from.address.local
            from.address != chat -> from.address.local
            else -> from.resource
        }
}

fun Message.calculateId() = copy(
    id = (text + from + to + timestamp).md5()
)

private fun String.md5() = MD5.digest(toByteArray()).printHexBinary()

private val MD5 = MessageDigest.getInstance("MD5")

private fun ByteArray.printHexBinary(): String =
    asSequence().map(Byte::toInt).fold(StringBuilder(size * 2)) { acc, i ->
        acc.append(HEX_CHARS[i shr 4 and 0xF]).append(HEX_CHARS[i and 0xF])
    }.toString()

private val HEX_CHARS = "0123456789ABCDEF".toCharArray()
