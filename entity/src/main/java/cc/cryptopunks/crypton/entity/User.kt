package cc.cryptopunks.crypton.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Entity
data class User(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @Embedded val resourceId: ResourceId = ResourceId.Empty
) {

    val remoteId get() = resourceId.remoteId

    interface Api {
        val getContacts: GetContacts
        val invite: Invite
        val invited: Invited

        interface GetContacts : () -> List<User>

        interface Invite : (User) -> Unit
        interface Invited : (User) -> Unit
    }


    interface Dao {
        @Query(
            """
            select * from User 
            inner join ConversationUser on User.id = ConversationUser.userId
            where ConversationUser.conversationId = :conversationId
            """
        )
        fun flowaList(conversationId: Long): Flow<List<User>>
    }

    companion object {
        val Empty = User()

        fun from(string: String) = User(
            resourceId = ResourceId.from(string)
        )
    }
}