package cc.cryptopunks.crypton.entity

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(
    indices = [Index(
        value = ["local", "domain"],
        unique = true
    )]
)
data class User(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @Embedded val remoteId: RemoteId = RemoteId.Empty
) {

    constructor(string: String) : this(
        remoteId = RemoteId.from(string)
    )

    interface Api {
        val getContacts: GetContacts
        val addContact: AddContact
        val invite: Invite
        val invited: Invited

        interface GetContacts : () -> List<User>
        interface AddContact : (User) -> Unit
        interface Invite : (RemoteId) -> Unit
        interface Invited : (RemoteId) -> Unit
    }

    @androidx.room.Dao
    interface Dao {
        @Insert
        suspend fun insert(user: User)

        @Insert(onConflict = OnConflictStrategy.IGNORE)
        suspend fun insertIfNeeded(list: List<User>) : List<Long>

        @Query(
            """
            select * from User 
            inner join ChatUser on User.id = ChatUser.userId
            where ChatUser.chatId = :chatId
            """
        )
        fun flowListByChatId(chatId: Long): Flow<List<User>>

        @Query(
            """
            select * from User 
            inner join AccountUser on User.id = AccountUser.userId
            where AccountUser.accountId = :accountId
            """
        )
        fun getByAccountId(accountId: Long): User
    }

    companion object {
        val Empty = User()
    }
}