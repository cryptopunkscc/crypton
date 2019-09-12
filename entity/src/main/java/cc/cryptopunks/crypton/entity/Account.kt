package cc.cryptopunks.crypton.entity

import androidx.room.*
import io.reactivex.Flowable
import kotlinx.coroutines.flow.Flow

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
    interface Dao {

        @Query("select id from account where id = :id")
        fun contains(id: Long): Long?

        @Query("select * from account where id = :id")
        fun get(id: Long): Account

        @Insert
        fun insert(account: Account): Long?

        @Update
        fun update(account: Account)

        @Delete
        fun delete(vararg accounts: Account)

        @Query("select * from account")
        fun observeChanges(): Flowable<Account>

        @Query("select * from account")
        fun list(): List<Account>

        @Query("select * from account")
        fun flowableList(): Flowable<List<Account>>

        @Query("select * from account")
        fun flowList(): Flow<List<Account>>
    }

    companion object {
        val Empty = Account()
    }
}

