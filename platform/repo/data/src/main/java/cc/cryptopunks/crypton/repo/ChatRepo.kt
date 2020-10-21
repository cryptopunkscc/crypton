package cc.cryptopunks.crypton.repo

import androidx.paging.DataSource
import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.entity.ChatData
import cc.cryptopunks.crypton.entity.ChatUserData
import cc.cryptopunks.crypton.entity.UserData
import cc.cryptopunks.crypton.entity.chatData
import cc.cryptopunks.crypton.entity.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ChatRepo(
    private val chatDao: ChatData.Dao,
    private val chatUserDao: ChatUserData.Dao,
    private val userDao: UserData.Dao
) : Chat.Repo {

    override suspend fun get(address: Address): Chat =
        requireNotNull(chatDao.get(address.id)).toDomain()

    override suspend fun contains(address: Address): Boolean =
        chatDao.contains(address.id) != null

    override suspend fun list(): List<Chat> =
        chatDao.list().map { it.toDomain() }

    suspend fun list(accounts: List<Address>): List<Chat> =
        chatDao.list(accounts.map(Address::id)).map { it.toDomain() }

    override suspend fun insert(chat: Chat)  =
        chat.run {
            chatDao.insert(chatData())
            insertUsersIfNeeded()
        }

    override suspend fun insertIfNeeded(chat: Chat) {
        if (!contains(chat)) chat.apply {
            chatDao.insert(chatData())
            insertUsersIfNeeded()
        }
    }

    private suspend fun Chat.insertUsersIfNeeded() {
        userDao.insertIfNeeded(
            list = users.map { user ->
                UserData(user.id)
            }
        )
        chatUserDao.insertIfNeeded(
            list = users.map { user ->
                ChatUserData(
                    id = user.id,
                    chatId = address.id
                )
            }
        )
    }

    override fun dataSourceFactory(): DataSource.Factory<Int, Chat> =
        chatDao.dataSourceFactory().map { chat -> chat.toDomain() }

    override fun flowList(): Flow<List<Chat>> =
        chatDao.flowList().map { list -> list.map { data -> data.toDomain() } }

    override suspend fun delete(chats: List<Address>): Unit =
        chatDao.delete(chats.map { it.id })

    override suspend fun deleteAll(): Unit =
        chatDao.deleteAll()

    suspend fun contains(chat: Chat) =
        chatDao.contains(chat.address.id) != null
}
