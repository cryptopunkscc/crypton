package cc.cryptopunks.crypton.repo

import androidx.paging.DataSource
import cc.cryptopunks.crypton.entity.*

internal class ChatRepo(
    private val chatDao: ChatData.Dao,
    private val chatUserDao: ChatUserData.Dao,
    private val userDao: UserData.Dao
) : Chat.Repo {

    override fun dataSourceFactory(): DataSource.Factory<Int, Chat> =
        chatDao.dataSourceFactory().map { chat -> chat.toDomain() }

    override suspend fun insert(chat: Chat): Long =
        chatDao.insert(chat.chatData()).also { chatId ->
            userDao.insertIfNeeded(
                list = chat.users.map { user ->
                    user.userData()
                }
            )
            chatUserDao.insertIfNeeded(
                list = chat.users.map { user ->
                    user.chatUserData(chatId = chatId)
                }
            )
        }

    override fun delete(chat: Chat): Unit =
        chatDao.delete(chat.chatData())

    override fun deleteAll(): Unit =
        chatDao.deleteAll()
}