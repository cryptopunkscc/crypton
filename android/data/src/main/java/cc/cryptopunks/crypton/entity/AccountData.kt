package cc.cryptopunks.crypton.entity

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "account")
internal data class AccountData(
    @PrimaryKey val id: AddressData,
    val password: String,
    val status: Account.Status = Account.Status.Disconnected,
    val updateAt: Long
) {

    class StatusConverter {
        @TypeConverter
        fun toInt(status: Account.Status) = status.ordinal

        @TypeConverter
        fun toStatus(ordinal: Int) = Account.Status.values()[ordinal]
    }

    @androidx.room.Dao
    interface Dao {

        @Query("select id from account where id = :id")
        fun contains(id: String): String?

        @Query("select * from account where id = :id")
        fun get(id: String): AccountData

        @Insert
        fun insert(data: AccountData)

        @Update
        fun update(data: AccountData)

        @Delete
        fun delete(data: AccountData)

        @Query("select * from account")
        fun list(): List<AccountData>

        @Query("select * from account")
        fun flowList(): Flow<List<AccountData>>
    }
}

internal fun AccountData.toDomain() = Account(
    address = Address.from(id),
    status = status,
    password = password,
    updateAt = updateAt
)

internal fun Account.chatData() = AccountData(
    id = address.id,
    password = password,
    status = status,
    updateAt = updateAt
)