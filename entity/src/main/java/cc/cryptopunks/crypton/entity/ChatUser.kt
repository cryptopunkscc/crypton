package cc.cryptopunks.crypton.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Insert

@Entity(
    primaryKeys = [
        "chatId",
        "userId"
    ],
    foreignKeys = [
        ForeignKey(
            entity = Chat::class,
            parentColumns = ["id"],
            childColumns = ["chatId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ChatUser(
    val chatId: Long,
    val userId: Long
) {

    constructor(conversation: Chat, user: User) : this(
        chatId = conversation.id,
        userId = user.id
    )

    @androidx.room.Dao
    interface Dao:
        User.Dao,
        Chat.Dao {

        @Insert
        fun insert(entity: ChatUser) : Long

        @Insert
        fun insert(list: List<ChatUser>) : List<Long>
    }
}