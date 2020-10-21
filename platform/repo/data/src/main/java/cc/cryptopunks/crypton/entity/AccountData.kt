package cc.cryptopunks.crypton.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update
import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Password
import com.j256.ormlite.field.DataType
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import kotlinx.coroutines.flow.Flow

@DatabaseTable(tableName = "account")
@Entity(tableName = "account")
data class AccountData(
    @DatabaseField(id = true)
    @PrimaryKey
    val id: AddressData = "",
    @DatabaseField(dataType = DataType.BYTE_ARRAY)
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    val password: ByteArray = ByteArray(0)
) {

    @androidx.room.Dao
    interface Dao {

        @Query("select id from account where id = :id")
        suspend fun contains(id: AddressData): String?

        @Query("select * from account where id = :id")
        suspend fun get(id: AddressData): AccountData?

        @Insert
        suspend fun insert(entity: AccountData)

        @Update
        suspend fun update(entity: AccountData)

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

fun AccountData.toDomain() = Account(
    address = Address.from(id),
    password = Password(password)
)

fun Account.accountData() = AccountData(
    id = address.id,
    password = password.byteArray
)
