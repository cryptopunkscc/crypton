package cc.cryptopunks.crypton.repo

import androidx.paging.DataSource
import cc.cryptopunks.crypton.entity.*

internal class ChatRepo(
    private val chatDao: ChatData.Dao,
    private val chatUserDao: ChatUserData.Dao,
    private val userDao: UserData.Dao
) : Chat.Repo {

    override suspend fun get(id: Long): Chat =
        chatDao.get(id).toDomain()

    override suspend fun get(address: Address): Chat? =
        chatDao.get(address.id).toDomain()

    override suspend fun insert(chat: Chat): Chat =
        chatDao.insert(chat.chatData()).let { chatId ->
            chat.insertUsersIfNeeded(chatId)
            chat.copy(id = chatId)
        }

    override suspend fun insertIfNeeded(chat: Chat): Chat? =
        chatDao.insertIfNeeded(chat.chatData())?.let { chatId ->
            chat.insertUsersIfNeeded(chatId)
            chat.copy(id = chatId)
        }

    private suspend fun Chat.insertUsersIfNeeded(chatId: Long) {
        userDao.insertIfNeeded(
            list = users.map { user ->
                user.userData()
            }
        )
        chatUserDao.insertIfNeeded(
            list = users.map { user ->
                user.chatUserData(chatId = chatId)
            }
        )
    }

    override fun dataSourceFactory(): DataSource.Factory<Int, Chat> =
        chatDao.dataSourceFactory().map { chat -> chat.toDomain() }

    override suspend fun delete(chat: Chat): Unit =
        chatDao.delete(chat.chatData())

    override suspend fun deleteAll(): Unit =
        chatDao.deleteAll()
}