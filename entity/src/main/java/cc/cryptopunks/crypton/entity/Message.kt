package cc.cryptopunks.crypton.entity

import androidx.paging.DataSource
import androidx.room.*
import cc.cryptopunks.crypton.util.RxPublisher
import kotlinx.coroutines.flow.Flow

typealias ApiMessage = Message

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Chat::class,
            parentColumns = ["id"],
            childColumns = ["chatId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Message(
    @PrimaryKey val id: String = "",
    val chatId: Long = 0,
    val stanzaId: String = "",
    val text: String = "",
    val timestamp: Long = 0,
    @Embedded(prefix = "from") val from: ResourceId = ResourceId.Empty,
    @Embedded(prefix = "to") val to: ResourceId = ResourceId.Empty
) {

    interface Api {
        val sendMessage: Send
        val messagePublisher: Publisher

        interface Send : (RemoteId, String) -> Unit
        interface Publisher : RxPublisher<Message>
    }

    @androidx.room.Dao
    interface Dao {
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun insertOrUpdate(vararg message: Message): List<Long>

        @Query("select * from Message where chatId == :chatId order by timestamp")
        fun flowLatest(chatId: Long): Flow<Message>

        @Query("select * from Message")
        fun dataSourceFactory(): DataSource.Factory<Int, Message>
    }

    companion object {
        val Empty = Message()
    }
}