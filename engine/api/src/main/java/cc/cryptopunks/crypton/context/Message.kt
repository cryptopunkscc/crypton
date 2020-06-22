package cc.cryptopunks.crypton.context

import androidx.paging.DataSource
import kotlinx.coroutines.flow.Flow
import java.security.MessageDigest

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

    object Service {
        object FetchArchived
        data class Save(val messages: List<Message>)
    }

    interface Net {
        suspend fun sendMessage(message: Message)
        fun incomingMessages(): Flow<Event>
        fun readArchived(query: ReadArchived.Query): Flow<List<Message>>

        object ReadArchived {
            data class Query(
                val since: Long? = null,
                val afterUid: String? = null,
                val until: Long = System.currentTimeMillis(),
                val chat: Chat? = null
            )
        }

        data class Event(
            val message: Message
        ) : Api.Event
    }

    interface Repo {
        suspend fun insertOrUpdate(message: Message)
        suspend fun insertOrUpdate(messages: List<Message>)
        suspend fun latest(): Message?
        suspend fun get(id: String): Message?
        suspend fun delete(message: Message)
        suspend fun listUnread(): List<Message>
        suspend fun list(range: LongRange = 0..System.currentTimeMillis()): List<Message>
        fun flowLatest(chatAddress: Address? = null): Flow<Message>
        fun dataSourceFactory(chatAddress: Address): DataSource.Factory<Int, Message>
        suspend fun queuedList(): List<Message>
        fun unreadListFlow(): Flow<List<Message>>
        fun queuedListFlow(): Flow<List<Message>>
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
