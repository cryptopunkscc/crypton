package cc.cryptopunks.crypton.repo.ormlite.dao

import cc.cryptopunks.crypton.entity.AddressData
import cc.cryptopunks.crypton.entity.ChatData
import cc.cryptopunks.crypton.util.ormlite.CryptonDao
import cc.cryptopunks.crypton.util.ormlite.OrmLiteCryptonDao
import com.j256.ormlite.dao.Dao
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class ChatDao(
    private val read: CoroutineContext,
    private val write: CoroutineContext,
    private val dao: Dao<ChatData, String>,
) : ChatData.Dao,
    CryptonDao<ChatData, String> by OrmLiteCryptonDao(
        dao = dao,
        read = read,
        write = write,
    ) {

    override suspend fun list(ids: List<AddressData>): List<ChatData> = withContext(read) {
        dao.queryBuilder().where().`in`("id", ids).query()
    }
}
