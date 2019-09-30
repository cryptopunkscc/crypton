package cc.cryptopunks.crypton.entity

import androidx.room.*

@Entity(
    tableName = "chatUser",
    primaryKeys = [
        "id",
        "chatId"
    ],
    indices = [Index("chatId")],
    foreignKeys = [
        ForeignKey(
            entity = ChatData::class,
            parentColumns = ["id"],
            childColumns = ["chatId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UserData::class,
            parentColumns = ["id"],
            childColumns = ["id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ChatUserData(
    val id: AddressData,
    val chatId: Long
) {

    @androidx.room.Dao
    interface Dao {

        @Insert
        fun insert(entity: ChatUserData)

        @Insert(onConflict = OnConflictStrategy.IGNORE)
        fun insertIfNeeded(list: List<ChatUserData>)

        @Query("select * from chatUser where id = :id")
        fun get(id: String) : ChatUserData

        @Query("select * from chatUser where chatId = :chatId")
        fun listByChat(chatId: Long) : List<ChatUserData>
    }
}

fun ChatUserData.user() = User(
    address = Address.from(id)
)

fun User.chatUserData(chatId: Long) = ChatUserData(
    chatId = chatId,
    id = address.id
)