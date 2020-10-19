package cc.cryptopunks.crypton.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import cc.cryptopunks.crypton.context.address
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable

@DatabaseTable(tableName = "chatUser")
@Entity(
    tableName = "chatUser",
    primaryKeys = [
        "id",
        "chatId"
    ],
    indices = [
        Index("id"),
        Index("chatId")
    ],
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
    @DatabaseField(
        index = true,
        uniqueCombo = true,
        foreign = true,
        foreignAutoRefresh = true
    )
    val id: AddressData,

    @DatabaseField(
        index = true,
        uniqueCombo = true,
        foreign = true,
        foreignAutoRefresh = true
    )
    val chatId: AddressData
) {

    @androidx.room.Dao
    interface Dao {

        @Insert
        suspend fun insert(entity: ChatUserData)

        @Insert(onConflict = OnConflictStrategy.IGNORE)
        suspend fun insertIfNeeded(list: List<ChatUserData>)

        @Query("select * from chatUser where id = :id")
        suspend fun get(id: AddressData) : ChatUserData?

        @Query("select * from chatUser where chatId = :chatId")
        suspend fun listByChat(chatId: AddressData) : List<ChatUserData>
    }
}

fun ChatUserData.user() = address(id)
