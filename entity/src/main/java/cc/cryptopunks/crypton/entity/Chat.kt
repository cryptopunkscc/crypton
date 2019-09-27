package cc.cryptopunks.crypton.entity

import androidx.paging.DataSource
import androidx.room.*

@Entity(
    foreignKeys = [ForeignKey(
        entity = Account::class,
        parentColumns = ["id"],
        childColumns = ["accountId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Chat(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String = "",
    val accountId: Long = 0
) {

    data class Exception(
        val conversations: List<Chat>,
        override val cause: Throwable
    ) : kotlin.Exception(cause) {
        constructor(
            conversation: Chat,
            cause: Throwable
        ) : this(listOf(conversation), cause)
    }

    @androidx.room.Dao
    interface Dao {

        @Query("select * from Chat")
        fun dataSourceFactory(): DataSource.Factory<Int, Chat>

        @Insert(onConflict = OnConflictStrategy.IGNORE)
        fun insertIfNeeded(conversation: Chat): Long?

        @Insert
        suspend fun insert(chat: Chat): Long

        @Insert
        suspend fun insert(chatList: List<Chat>): List<Long>

        @Delete
        fun delete(ids: List<Chat>)

        @Query("delete from Chat")
        fun deleteAll()

    }

    companion object {
        val Empty = Chat()
    }
}