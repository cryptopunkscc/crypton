package cc.cryptopunks.crypton.entity

import androidx.room.*
import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.User
import kotlinx.coroutines.flow.Flow

@Entity(
    tableName = "user"
)
internal data class UserData(
    @PrimaryKey val id: AddressData
) {

    @androidx.room.Dao
    interface Dao {
        @Insert
        suspend fun insert(user: UserData)

        @Insert(onConflict = OnConflictStrategy.IGNORE)
        suspend fun insertIfNeeded(list: List<UserData>)

        @Query("select * from User where id = :id")
        suspend fun getById(id: AddressData): UserData

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

internal fun UserData.user() = User(
    address = Address.from(id)
)

internal fun User.userData() = UserData(
    id = address.id
)