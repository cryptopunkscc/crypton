package cc.cryptopunks.crypton.entity

import androidx.room.*
import cc.cryptopunks.crypton.context.address
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import kotlinx.coroutines.flow.Flow

@DatabaseTable(tableName = "user")
@Entity(tableName = "user")
data class UserData(
    @PrimaryKey
    @DatabaseField(id = true)
    val id: AddressData = ""
) {

    @androidx.room.Dao
    interface Dao {
        @Insert
        suspend fun insert(entity: UserData)

        @Insert(onConflict = OnConflictStrategy.IGNORE)
        suspend fun insertIfNeeded(list: List<UserData>)

        @Query("select * from User where id = :id")
        suspend fun get(id: AddressData): UserData?

        @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
        @Query(
            """
            select * from user 
            inner join chatUser on user.id = chatUser.id
            where chatUser.chatId = :chatId
            """
        )
        fun flowListByChatId(chatId: AddressData): Flow<List<UserData>>
    }
}

fun UserData.user() = address(id)
