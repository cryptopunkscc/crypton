package cc.cryptopunks.crypton.repo.ormlite.dao

import androidx.paging.DataSource
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.entity.AddressData
import cc.cryptopunks.crypton.entity.MessageData
import cc.cryptopunks.crypton.util.ormlite.CryptonDao
import cc.cryptopunks.crypton.util.ormlite.DaoPositionalDataSource
import cc.cryptopunks.crypton.util.ormlite.OrmLiteCryptonDao
import cc.cryptopunks.crypton.util.ormlite.changesFlow
import com.j256.ormlite.dao.Dao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class MessageDao(
    private val read: CoroutineContext,
    private val write: CoroutineContext,
    private val dao: Dao<MessageData, String>,
) : MessageData.Dao,
    CryptonDao<MessageData, String> by OrmLiteCryptonDao(
        dao = dao,
        read = read,
        write = write
    ) {

    override fun latest(): MessageData? = dao
        .queryBuilder()
        .orderBy("timestamp", false)
        .queryForFirst()

    override fun latest(chatId: AddressData): MessageData? = dao
        .queryBuilder()
        .orderBy("timestamp", false).where()
        .eq("chatId", chatId)
        .queryForFirst()

    override fun flowLatest(): Flow<MessageData?> = dao
        .changesFlow { send(latest()) }

    override fun flowLatest(chatId: AddressData): Flow<MessageData?> = dao
        .changesFlow { send(latest(chatId)) }

    override fun dataSourceFactory(chatId: AddressData): DataSource.Factory<Int, MessageData> =
        DaoPositionalDataSource.Factory(dao) { eq("chatId", chatId) }

    override suspend fun listUnread(): List<MessageData> = withContext(read) {
        dao.queryBuilder().where()
            .eq("readAt", 0)
            .and()
            .eq("status", Message.Status.Received.name)
            .query()
    }

    private suspend fun listUnreadIds(chatId: AddressData): List<String> = withContext(read) {
        dao.queryBuilder()
            .selectColumns("id").where()
            .eq("chatId", chatId)
            .and()
            .eq("readAt", 0)
            .queryRaw()
            .mapNotNull { it.firstOrNull() }
    }

    override suspend fun list(
        latest: Long,
        oldest: Long,
    ): List<MessageData> = withContext(read) {
        dao.queryBuilder().where()
            .le("timestamp", latest)
            .and()
            .ge("timestamp", oldest)
            .query()
    }

    override suspend fun list(
        chat: AddressData,
        latest: Long,
        oldest: Long,
    ): List<MessageData> = withContext(read) {
        dao.queryBuilder().where()
            .eq("chatId", chat)
            .and()
            .le("timestamp", latest)
            .and()
            .ge("timestamp", oldest)
            .query()
    }

    private suspend fun listUnread(chatId: AddressData): List<MessageData> = withContext(read) {
        dao.queryBuilder().where()
            .eq("chatId", chatId)
            .and()
            .eq("readAt", 0)
            .query()
    }

    override suspend fun listByStatus(
        chat: AddressData,
        status: String,
    ): List<MessageData> = withContext(read) {
        dao.queryBuilder().where()
            .eq("chatId", chat)
            .and()
            .eq("status", status)
            .query()
    }

    override suspend fun listByType(
        chat: AddressData,
        type: String,
    ): List<MessageData> = withContext(read) {
        dao.queryBuilder().where()
            .eq("chatId", chat)
            .and()
            .eq("type", type)
            .query()
    }

    override suspend fun listQueued(): List<MessageData> = dao
        .queryForEq("status", Message.Status.Queued.name)

    override fun flowUnreadList(): Flow<List<MessageData>> = dao
        .changesFlow { send(listUnread()) }

    override fun flowUnreadList(chatId: AddressData): Flow<List<MessageData>> = dao
        .changesFlow { send(listUnread(chatId)) }

    override fun flowQueueList(): Flow<List<MessageData>> = dao
        .changesFlow { send(listQueued()) }

    override fun flowUnreadIds(chatId: AddressData): Flow<List<String>> = dao
        .changesFlow { send(listUnreadIds(chatId)) }
}
