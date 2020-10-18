package cc.cryptopunks.crypton.entity

import androidx.paging.DataSource
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.address
import kotlinx.coroutines.flow.Flow

@Entity(
    tableName = "chat"
)
internal data class ChatData(
    @PrimaryKey val id: AddressData,
    val accountId: AddressData,
    val title: String,
    val isMultiUser: Boolean
) {

    @androidx.room.Dao
    interface Dao {

        @Query("select * from chat where id = :id")
        suspend fun get(id: AddressData): ChatData

        @Insert
        suspend fun insert(data: ChatData)

        @Insert(onConflict = OnConflictStrategy.IGNORE)
        suspend fun insertIfNeeded(data: ChatData)

        @Query("delete from chat where id in (:ids)")
        suspend fun delete(ids: List<AddressData>)

        @Query("delete from chat")
        suspend fun deleteAll()

        @Query("select id from chat where id = :id")
        suspend fun contains(id: AddressData): AddressData?

        @Query("select * from chat where accountId in (:accountIds)")
        suspend fun list(accountIds: List<AddressData>): List<ChatData>

        @Query("select * from chat")
        suspend fun list(): List<ChatData>

        @Query("select * from chat")
        fun dataSourceFactory(): DataSource.Factory<Int, ChatData>

        @Query("select * from chat")
        fun flowList(): Flow<List<ChatData>>
    }
}

internal fun ChatData.toDomain(users: List<Address> = emptyList()) =
    Chat(
        title = title,
        address = address(id),
        account = address(accountId),
        users = users
    )

internal fun Chat.chatData() = ChatData(
    title = title,
    id = address.id,
    accountId = account.id,
    isMultiUser = isConference
)
