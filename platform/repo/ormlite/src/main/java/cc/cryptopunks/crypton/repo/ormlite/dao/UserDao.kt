package cc.cryptopunks.crypton.repo.ormlite.dao

import cc.cryptopunks.crypton.entity.AddressData
import cc.cryptopunks.crypton.entity.ChatUserData
import cc.cryptopunks.crypton.entity.UserData
import cc.cryptopunks.crypton.util.ormlite.CryptonDao
import cc.cryptopunks.crypton.util.ormlite.OrmLiteCryptonDao
import cc.cryptopunks.crypton.util.ormlite.changesFlow
import com.j256.ormlite.dao.Dao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class UserDao(
    private val read: CoroutineContext,
    private val write: CoroutineContext,
    private val userDao: Dao<UserData, String>,
    private val chatUserDao: Dao<ChatUserData, String>,
) : UserData.Dao,
    CryptonDao<UserData, String> by OrmLiteCryptonDao(
        dao = userDao,
        read = read,
        write = write
    ) {

    override fun flowListByChatId(chatId: AddressData): Flow<List<UserData>> =
        flowOf<Flow<List<UserData>>>(
            userDao.changesFlow { send(listByChatId(chatId)) },
            chatUserDao.changesFlow { send(listByChatId(chatId)) }
        ).flattenMerge()

    private suspend fun listByChatId(chatId: AddressData): List<UserData> = withContext(read) {
        userDao.queryRaw(
            "inner join chatUser on user.id = chatUser.id where chatUser.chatId = $chatId"
        ).mapNotNull { it.firstOrNull() }.map { UserData(it) }
    }
}
