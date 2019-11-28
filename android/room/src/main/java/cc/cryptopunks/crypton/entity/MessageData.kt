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
    val to: AddressData = EmptyAddressData
) {

    @androidx.room.Dao
    interface Dao {

        @Insert(onConflict = OnConflictStrategy.IGNORE)
        fun insert(messages: List<MessageData>)

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertOrUpdate(list: MessageData)

        @Query("select * from message order by timestamp desc")
        fun latest(): MessageData?

        @Query("select * from message where chatId == :chatId order by timestamp desc")
        fun flowLatest(chatId: AddressData): Flow<MessageData?>

        @Query("select * from message where chatId == :chatId order by timestamp desc")
        fun dataSourceFactory(chatId: AddressData): DataSource.Factory<Int, MessageData>
    }

    companion object {
        val Empty = MessageData()
    }
}

internal fun Message.messageData() = MessageData(
    id = id,
    chatId = chatAddress.id,
    stanzaId = stanzaId,
    timestamp = timestamp,
    text = text,
    from = from.id,
    to = to.id
)

internal fun MessageData.message() = Message(
    id = id,
    stanzaId = stanzaId,
    to = Resource.from(to),
    from = Resource.from(from),
    chatAddress = Address.from(chatId),
    text = text,
    timestamp = timestamp
)