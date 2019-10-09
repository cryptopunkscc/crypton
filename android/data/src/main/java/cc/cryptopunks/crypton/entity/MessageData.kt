package cc.cryptopunks.crypton.entity

import androidx.paging.DataSource
import androidx.room.*
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
    val chatId: Long = 0,
    val stanzaId: String = "",
    val text: String = "",
    val timestamp: Long = 0,
    val from: String = "",
    val to: String = ""
) {

    @androidx.room.Dao
    interface Dao {
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertOrUpdate(list: List<MessageData>)

        @Query("select * from message where chatId == :chatId order by timestamp")
        fun flowLatest(chatId: Long): Flow<MessageData?>

        @Query("select * from message where chatId == :chatId order by timestamp")
        fun dataSourceFactory(chatId: Long): DataSource.Factory<Int, MessageData>
    }

    companion object {
        val Empty = MessageData()
    }
}

internal fun Message.messageData(chatId: Long) = MessageData(
    id = id,
    stanzaId = stanzaId,
    chatId = chatId,
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