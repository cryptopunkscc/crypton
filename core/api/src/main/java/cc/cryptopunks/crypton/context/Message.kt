package cc.cryptopunks.crypton.context

import androidx.paging.DataSource
import cc.cryptopunks.crypton.dep
import cc.cryptopunks.crypton.util.md5
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow

typealias CryptonMessage = Message

val SessionScope.messageNet: Message.Net by dep()
val SessionScope.messageRepo: Message.Repo by dep()

data class Message(
    val id: String = "",
    val stanzaId: String = "",
    val body: String = "",
    val type: Type = body.parseType(),
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

    enum class Type {
        Text,
        Url,
        Info,
        State,
    }

    enum class Status {
        None,
        Queued,
        Sending,
        Sent,
        Error,
        Received,
        Read,
        Uploading,
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
        suspend fun get(id: String): Message?
        suspend fun delete(id: String)
        suspend fun delete(message: Message)
        suspend fun delete(message: List<Message>)
        suspend fun latest(): Message?
        suspend fun latest(chat: Address): Message?
        suspend fun list(chat: Address? = null, range: LongRange = 0..System.currentTimeMillis()): List<Message>
        suspend fun list(chat: Address, status: Status): List<Message>
        suspend fun list(chat: Address, type: Type): List<Message>
        suspend fun listUnread(): List<Message>
        suspend fun listQueued(): List<Message>
        fun dataSourceFactory(chatAddress: Address): DataSource.Factory<Int, Message>
        fun flowLatest(chatAddress: Address? = null): Flow<Message>
        fun flowListQueued(): Flow<List<Message>>
        fun flowListUnread(): Flow<List<Message>>
        fun flowUnreadCount(chatAddress: Address): Flow<Int>
        suspend fun notifyUnread()
    }

    interface Consumer {
        fun canConsume(message: Message): Boolean
    }

    object Notification {
        const val channelId = "Messages"
    }

    class Exception(message: String? = null) : kotlin.Exception(message)
}

private fun String.parseType() =
    if (split(":").firstOrNull() in knownProtocols)
        Message.Type.Url else
        Message.Type.Text

private val knownProtocols = listOf("http", "https", "aesgcm")

val Message.isUnread
    get() = readAt == 0L && status == Message.Status.Received

val Message.author: String
    get() = when {
        !chat.isConference -> from.address.local
        from.address != chat -> from.address.local
        else -> from.resource
    }

fun Message.validate(): Message = apply {
    chat.validate()
    from.address.validate()
    to.address.validate()
}

val Message.sender: String
    get() = when {
        from.address.isConference -> from.resource
        else -> from.address.local
    }

fun Message.isFrom(address: Address): Boolean = when {
    from.address.isConference -> from.resource == address.local
    else -> from.address == address
}

fun Message.calculateId(): Message = copy(
    id = (body + from + to + timestamp).md5()
)

internal val schemeRegex = Regex("^[a-z]+:.*")
internal val uriRegex = Regex("^[a-z]+://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]")

fun createCryptonMessage(
    id: String,
    stanzaId: String,
    from: Resource,
    to: Resource,
    body: String,
    status: Message.Status = Message.Status.None,
    timestamp: Long = System.currentTimeMillis(),
    type: Message.Type? = null,
    chat: Address? = null,
    encrypted: Boolean = true
) = CryptonMessage(
    id = id,
    stanzaId = stanzaId,
    body = body,
    from = from,
    to = to,
    timestamp = timestamp,
    status = status,
    type = type ?: body.parseType(),
    chat = chat ?: when {
        type == Message.Type.State ->
            if (to.address.isConference) to.address
            else from.address
        status == Message.Status.Received -> from.address
        status == Message.Status.Sent -> to.address
        else -> Address.Empty
    },
    encrypted = encrypted
)
