package cc.cryptopunks.crypton.entity

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "account")
internal data class AccountData(
    @PrimaryKey val id: AddressData,
    val password: String
) {

    @androidx.room.Dao
    interface Dao {

        @Query("select id from account where id = :id")
        suspend fun contains(id: String): String?

        @Query("select * from account where id = :id")
        suspend fun get(id: String): AccountData

        @Insert
        suspend fun insert(data: AccountData)

        @Update
        suspend fun update(data: AccountData)

        @Delete
        suspend fun delete(data: AccountData)

        @Query("select * from account")
        suspend fun list(): List<AccountData>

        @Query("select * from account")
        fun flowList(): Flow<List<AccountData>>
    }
}

internal fun AccountData.toDomain() = Account(
    address = Address.from(id),
    password = password
)

internal fun Account.chatData() = AccountData(
    id = address.id,
    password = password
)