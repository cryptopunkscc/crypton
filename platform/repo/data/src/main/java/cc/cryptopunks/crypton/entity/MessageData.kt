package cc.cryptopunks.crypton.entity

import androidx.paging.DataSource
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.address
import cc.cryptopunks.crypton.context.resource
import com.j256.ormlite.field.DataType
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import kotlinx.coroutines.flow.Flow

@DatabaseTable(tableName = "message")
@Entity(
    tableName = "message",
    indices = [Index("chatId")],
    foreignKeys = [
        ForeignKey(
            entity = ChatData::class,
            parentColumns = ["id"],
            childColumns = ["chatId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class MessageData(
    @DatabaseField(id = true)
    @PrimaryKey
    val id: String = "",
    @DatabaseField
    val stanzaId: String = "",
    @DatabaseField(dataType = DataType.LONG_STRING)
    val text: String = "",
    @DatabaseField
    val type: String = Message.Type.Text.name,
    @DatabaseField
    val timestamp: Long = 0,
    @DatabaseField(index = true)
    val chatId: AddressData = EmptyAddressData,
    @DatabaseField
    val from: AddressData = EmptyAddressData,
    @DatabaseField
    val to: AddressData = EmptyAddressData,
    @DatabaseField
    val status: String = "",
    @DatabaseField
    val readAt: Long = 0,
    @DatabaseField
    val encrypted: Boolean = true,
) {

    @androidx.room.Dao
    interface Dao {

        @Insert(onConflict = OnConflictStrategy.IGNORE)
        suspend fun insert(list: List<MessageData>)

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertOrUpdate(entity: MessageData)

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertOrUpdate(list: List<MessageData>)

        @Query("select * from message where id == :id")
        suspend fun get(id: String): MessageData?

        @Query("delete from message where id == :id")
        suspend fun delete(id: String)

        @Query("select * from message order by timestamp desc")
        fun latest(): MessageData?

        @Query("select * from message where chatId == :chatId order by timestamp desc")
        fun latest(chatId: AddressData): MessageData?

        @Query("select * from message order by timestamp desc")
        fun flowLatest(): Flow<MessageData?>

        @Query("select * from message where chatId == :chatId order by timestamp desc")
        fun flowLatest(chatId: AddressData): Flow<MessageData?>

        @Query("select * from message where chatId == :chatId order by timestamp desc")
        fun dataSourceFactory(chatId: AddressData): DataSource.Factory<Int, MessageData>

        @Query("select * from message where timestamp <= :latest and timestamp >= :oldest")
        suspend fun list(latest: Long, oldest: Long): List<MessageData>

        @Query("select * from message where chatId == :chat and timestamp <= :latest and timestamp >= :oldest")
        suspend fun list(chat: AddressData, latest: Long, oldest: Long): List<MessageData>

        @Query("select * from message where chatId == :chat and status == :status")
        suspend fun listByStatus(chat: AddressData, status: String): List<MessageData>

        @Query("select * from message where chatId == :chat and type == :type")
        suspend fun listByType(chat: AddressData, type: String): List<MessageData>

        @Query("select * from message where readAt == 0 and status == 'Received'")
        suspend fun listUnread(): List<MessageData>

        @Query("select * from message where status == 'Queued'")
        suspend fun listQueued(): List<MessageData>

        @Query("select * from message where readAt == 0")
        fun flowUnreadList(): Flow<List<MessageData>>

        @Query("select * from message where readAt == 0 and chatId == :chatId and status == 'Received'")
        fun flowUnreadList(chatId: AddressData): Flow<List<MessageData>>

        @Query("select * from message where status == 'Queued'")
        fun flowQueueList(): Flow<List<MessageData>>

        @Query("select id from message where readAt == 0 and chatId == :chatId and status == 'Received'")
        fun flowUnreadIds(chatId: AddressData): Flow<List<String>>
    }
}

private inline fun <reified T> typeName() = T::class.java.name

internal fun Message.messageData() = MessageData(
    id = id,
    chatId = chat.id,
    stanzaId = stanzaId,
    timestamp = timestamp,
    text = body,
    type = type.name,
    from = from.id,
    to = to.id,
    status = status.name,
    readAt = readAt,
    encrypted = encrypted
)

internal fun MessageData.message() = Message(
    id = id,
    stanzaId = stanzaId,
    to = resource(to),
    from = resource(from),
    chat = address(chatId),
    body = text,
    type = Message.Type.valueOf(type),
    timestamp = timestamp,
    status = messageStatus,
    readAt = readAt,
    encrypted = encrypted
)

private val MessageData.messageStatus get() = Message.Status.valueOf(status)
