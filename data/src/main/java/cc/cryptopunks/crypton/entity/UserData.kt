package cc.cryptopunks.crypton.entity

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(
    tableName = "user"
)
data class UserData(
    @PrimaryKey val id: AddressData
) {

    @androidx.room.Dao
    interface Dao {
        @Insert
        suspend fun insert(user: UserData)

        @Insert(onConflict = OnConflictStrategy.IGNORE)
        suspend fun insertIfNeeded(list: List<UserData>)

        @Query(
            """
            select * from user 
            inner join chatUser on user.id = chatUser.id
            where chatUser.chatId = :chatId
            """
        )
        fun flowListByChatId(chatId: Long): Flow<List<UserData>>

        @Query("select * from User where id = :id")
        fun getById(id: String): UserData
    }
}

fun UserData.user() = User(
    address = Address.from(id)
)

fun User.userData() = UserData(
    id = address.id
)