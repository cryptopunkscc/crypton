package cc.cryptopunks.crypton.entity

import androidx.room.*
import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.context.Address
import kotlinx.coroutines.flow.Flow
import java.nio.CharBuffer

@Entity(tableName = "account")
internal data class AccountData(
    @PrimaryKey val id: AddressData,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB) val password: ByteArray
) {

    @androidx.room.Dao
    interface Dao {

        @Query("select id from account where id = :id")
        suspend fun contains(id: AddressData): String?

        @Query("select * from account where id = :id")
        fun get(id: AddressData): AccountData

        @Insert
        suspend fun insert(data: AccountData)

        @Update
        suspend fun update(data: AccountData)

        @Query("delete from account where id = :id")
        suspend fun delete(id: AddressData)

        @Query("select * from account")
        suspend fun list(): List<AccountData>

        @Query("select * from account")
        fun flowList(): Flow<List<AccountData>>
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AccountData

        if (id != other.id) return false
        if (!password.contentEquals(other.password)) return false

        return true
    }

    override fun hashCode() = id.hashCode()
}

internal fun AccountData.toDomain() = Account(
    address = Address.from(id),
    password = password
        .map { it.toChar() }
        .toCharArray()
        .let { CharBuffer.wrap(it) }
)

internal fun Account.chatData() = AccountData(
    id = address.id,
    password = password
        .map { it.toByte() }
        .toByteArray()
)