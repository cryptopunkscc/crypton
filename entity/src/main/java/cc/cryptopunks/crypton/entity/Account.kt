package cc.cryptopunks.crypton.entity

import androidx.room.*
import io.reactivex.Observable
import io.reactivex.Single

@Entity(
    indices = [Index(
        value = ["domain", "userName"],
        unique = true
    )]
)
data class Account constructor(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val domain: String = "",
    val status: Status = Status.Disconnected,
    @Embedded val credentials: Credentials = Credentials.Empty
) {

    val remoteId
        @Ignore get() = RemoteId(
            local = credentials.userName,
            domain = domain
        )

    enum class Status {
        Disconnected,
        RequestConnect,
        RequestDisconnect,
        Connecting,
        Connected,
        Error;

        class Converter {
            @TypeConverter
            fun toInt(status: Status) = status.ordinal

            @TypeConverter
            fun toStatus(ordinal: Int) = values()[ordinal]
        }
    }

    data class Credentials(
        val userName: String = "",
        val password: String = ""
    ) {
        companion object {
            val Empty = Credentials()
        }
    }

    data class Exception(
        val account: Account,
        override val cause: Throwable
    ) : kotlin.Exception(

        account.toString(),
        cause
    )

    @androidx.room.Dao
    abstract class Dao {

        @Query("select id from account where id = :id")
        abstract fun contains(id: Long): Long?

        @Query("select * from account where id = :id")
        abstract fun get(id: Long): Account

        @Insert
        abstract fun insert(account: Account): Long?

        @Update
        abstract fun update(account: Account)

        @Delete
        abstract fun delete(vararg accounts: Account)

        @Query("select * from account")
        abstract fun observeChanges(): Observable<Account>

        @Query("select * from account")
        abstract fun getAll(): List<Account>

        @Query("select * from account")
        abstract fun getAllSingle(): Single<List<Account>>

        @Query("select * from account")
        abstract fun observeList(): Observable<List<Account>>

        @Query("select id from account")
        abstract fun observeIdList(): Observable<List<Long>>
    }

    companion object {
        val Empty = Account()
    }
}

