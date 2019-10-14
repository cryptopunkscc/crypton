package cc.cryptopunks.crypton.entity

import androidx.paging.DataSource
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(
    tableName = "message",
    indices = [Index("to")],
    foreignKeys = [
        ForeignKey(
            entity = ChatData::class,
            parentColumns = ["id"],
            childColumns = ["to"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
internal data class MessageData(
    @PrimaryKey val id: String = "",
    val stanzaId: String = "",
    val text: String = "",
    val timestamp: Long = 0,
    val from: AddressData = "",
    val to: AddressData = ""
) {

    @androidx.room.Dao
    interface Dao {
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertOrUpdate(list: List<MessageData>)

        @Query("select * from message where `to` == :chatId order by timestamp")
        fun flowLatest(chatId: AddressData): Flow<MessageData?>

        @Query("select * from message where `to` == :chatId order by timestamp")
        fun dataSourceFactory(chatId: AddressData): DataSource.Factory<Int, MessageData>
    }

    companion object {
        val Empty = MessageData()
    }
}

internal fun Message.messageData() = MessageData(
    id = id,
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
    text = text,
    timestamp = timestamp
)