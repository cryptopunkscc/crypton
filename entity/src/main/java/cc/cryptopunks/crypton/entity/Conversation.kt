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
data class Conversation(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String = "",
    val accountId: Long = 0
) {

    data class Exception(
        val conversations: List<Conversation>,
        override val cause: Throwable
    ) : kotlin.Exception(cause) {
        constructor(
            conversation: Conversation,
            cause: Throwable
        ) : this(listOf(conversation), cause)
    }

    @androidx.room.Dao
    interface Dao {

        @Query("select * from Conversation")
        fun dataSourceFactory(): DataSource.Factory<Int, Conversation>

        @Insert(onConflict = OnConflictStrategy.IGNORE)
        fun insertIfNeeded(conversation: Conversation): Long?

        @Insert
        fun insert(conversation: List<Conversation>): List<Long>

        @Delete
        fun delete(ids: List<Conversation>)
    }

    companion object {
        val Empty = Conversation()
    }
}