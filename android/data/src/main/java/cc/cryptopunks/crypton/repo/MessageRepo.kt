package cc.cryptopunks.crypton.repo

import androidx.paging.DataSource
import cc.cryptopunks.crypton.entity.*
import cc.cryptopunks.crypton.entity.MessageData
import cc.cryptopunks.crypton.entity.message
import cc.cryptopunks.crypton.entity.messageData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

internal class MessageRepo(
    private val dao: MessageData.Dao
) : Message.Repo {

    override suspend fun insertOrUpdate(chatId: Long, vararg message: Message) =
        dao.insertOrUpdate(message.map { it.messageData(chatId) })

    override fun flowLatest(chatId: Long): Flow<Message> =
        dao.flowLatest(chatId).filterNotNull().map { it.message() }

    override fun dataSourceFactory(chat: Chat): DataSource.Factory<Int, Message> =
        dao.dataSourceFactory(chat.id).map { it?.message() }
}