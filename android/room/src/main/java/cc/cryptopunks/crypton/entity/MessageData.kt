package cc.cryptopunks.crypton.entity

import androidx.paging.DataSource
import androidx.room.*
import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.Resource
import kotlinx.coroutines.flow.Flow

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
internal data class MessageData(
    @PrimaryKey val id: String = "",
    val stanzaId: String = "",
    val text: String = "",
    val timestamp: Long = 0,
    val chatId: AddressData = EmptyAddressData,
    val from: AddressData = EmptyAddressData,
    val to: AddressData = EmptyAddressData,
    val status: String = "",
    val readAt: Long = 0
) {

    @androidx.room.Dao
    interface Dao {

        @Insert(onConflict = OnConflictStrategy.IGNORE)
        suspend fun insert(messages: List<MessageData>)

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertOrUpdate(data: MessageData)

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertOrUpdate(list: List<MessageData>)

        @Query("select * from message where id == :id")
        fun get(id: String): MessageData?

        @Query("select * from message order by timestamp desc")
        fun latest(): MessageData?

        @Query("select * from message order by timestamp desc")
        fun flowLatest(): Flow<MessageData?>

        @Query("select * from message where chatId == :chatId order by timestamp desc")
        fun flowLatest(chatId: AddressData): Flow<MessageData?>

        @Query("select * from message where chatId == :chatId order by timestamp desc")
        fun dataSourceFactory(chatId: AddressData): DataSource.Factory<Int, MessageData>

        @Query("delete from message where id == :id")
        fun delete(id: String)

        @Query("select * from message where readAt == 0 and status == 'Received'")
        suspend fun listUnread(): List<MessageData>

        @Query("select * from message where timestamp <= :latest and timestamp >= :oldest")
        suspend fun list(latest: Long, oldest: Long): List<MessageData>

        @Query("select * from message where readAt == 0")
        fun flowUnreadList(): Flow<List<MessageData>>

        @Query("select * from message where readAt == 0 and chatId == :chatId and status == 'Received'")
        fun flowUnreadList(chatId: AddressData): Flow<List<MessageData>>

        @Query("select * from message where status == 'Queued'")
        fun queueList(): List<MessageData>

        @Query("select * from message where status == 'Queued'")
        fun flowQueueList(): Flow<List<MessageData>>

        @Query("select id from message where readAt == 0 and chatId == :chatId and status == 'Received'")
        fun flowUnreadIds(chatId: AddressData): Flow<List<String>>
    }
}

internal fun Message.messageData() = MessageData(
    id = id,
    chatId = chatAddress.id,
    stanzaId = stanzaId,
    timestamp = timestamp,
    text = text,
    from = from.id,
    to = to.id,
    status = status.name,
    readAt = readAt
)

internal fun MessageData.message() = Message(
    id = id,
    stanzaId = stanzaId,
    to = Resource.from(to),
    from = Resource.from(from),
    chatAddress = Address.from(chatId),
    text = text,
    timestamp = timestamp,
    status = Message.Status.valueOf(status),
    readAt = readAt
)
