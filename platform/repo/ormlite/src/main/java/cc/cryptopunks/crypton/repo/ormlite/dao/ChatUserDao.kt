package cc.cryptopunks.crypton.repo.ormlite.dao

import cc.cryptopunks.crypton.entity.AddressData
import cc.cryptopunks.crypton.entity.ChatUserData
import cc.cryptopunks.crypton.util.ormlite.CryptonDao
import cc.cryptopunks.crypton.util.ormlite.OrmLiteCryptonDao
import com.j256.ormlite.dao.Dao
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class ChatUserDao(
    private val read: CoroutineContext,
    write: CoroutineContext,
    private val dao: Dao<ChatUserData, String>,
) : ChatUserData.Dao,
    CryptonDao<ChatUserData, String> by OrmLiteCryptonDao(
        dao = dao,
        read = read,
        write = write
    ) {

    override suspend fun listByChat(
        chatId: AddressData
    ): List<ChatUserData> = withContext(read) {
        dao.queryForEq("chatId", chatId)
    }
}
