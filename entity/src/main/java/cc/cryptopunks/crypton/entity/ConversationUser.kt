package cc.cryptopunks.crypton.entity

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    primaryKeys = [
        "conversationId",
        "userId"
    ],
    foreignKeys = [
        ForeignKey(
            entity = Conversation::class,
            parentColumns = ["id"],
            childColumns = ["conversationId"],
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
data class ConversationUser(
    val conversationId: Long,
    val userId: Long
) {

    constructor(conversation: Conversation,  user: User) : this(
        conversationId = conversation.id,
        userId = user.id
    )

    @androidx.room.Dao
    interface Dao {
        fun insert(entity: ConversationUser)
    }
}