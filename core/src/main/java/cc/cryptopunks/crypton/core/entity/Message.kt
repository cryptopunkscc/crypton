package cc.cryptopunks.crypton.core.entity

import androidx.room.*
import io.reactivex.Observable

@Entity(
    foreignKeys = [ForeignKey(
        entity = Conversation::class,
        parentColumns = ["id"],
        childColumns = ["conversationId"]
    )]
)
data class Message(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val conversationId: Long = 0,
    val text: String = "",
    val timestamp: Long = System.currentTimeMillis()
) {
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