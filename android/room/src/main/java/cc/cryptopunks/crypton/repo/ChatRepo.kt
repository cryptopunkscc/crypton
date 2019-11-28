package cc.cryptopunks.crypton.repo

import androidx.paging.DataSource
import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.entity.*

internal class ChatRepo(
    private val chatDao: ChatData.Dao,
    private val chatUserDao: ChatUserData.Dao,
    private val userDao: UserData.Dao
) : Chat.Repo {

    override suspend fun get(address: Address): Chat =
        chatDao.get(address.id).toDomain()

    override suspend fun list(addresses: List<Address>): List<Chat> =
        chatDao.list(addresses.map(Address::id)).map { it.toDomain() }

    override suspend fun insert(chat: Chat)  {
        chat.apply {
            chatDao.insert(chatData())
            insertUsersIfNeeded()
        }
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
                user.userData()
            }
        )
        chatUserDao.insertIfNeeded(
            list = users.map { user ->
                user.chatUserData(chatId = address.id)
            }
        )
    }

    override fun dataSourceFactory(): DataSource.Factory<Int, Chat> =
        chatDao.dataSourceFactory().map { chat -> chat.toDomain() }

    override suspend fun delete(chat: Chat): Unit =
        chatDao.delete(chat.chatData())

    override suspend fun deleteAll(): Unit =
        chatDao.deleteAll()

    suspend fun contains(chat: Chat) =
        chatDao.contains(chat.address.id) != null
}