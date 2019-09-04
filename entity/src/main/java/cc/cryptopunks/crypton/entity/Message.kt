package cc.cryptopunks.crypton.entity

import androidx.room.*
import cc.cryptopunks.crypton.util.RxPublisher
import io.reactivex.Observable

typealias ApiMessage = Message

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Conversation::class,
            parentColumns = ["id"],
            childColumns = ["conversationId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Message(
    @PrimaryKey val id: String = "",
    val conversationId: Long = 0,
    val stanzaId: String = "",
    val text: String = "",
    val timestamp: Long = 0,
    @Embedded(prefix = "from") val from: RemoteId = RemoteId.Empty,
    @Embedded(prefix = "to") val to: RemoteId = RemoteId.Empty
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


        @Query("select * from Message where conversationId == :conversationId order by timestamp")
        fun lastMessage(conversationId: Long): Observable<Message>
    }

    companion object {
        val Empty = Message()
    }
}