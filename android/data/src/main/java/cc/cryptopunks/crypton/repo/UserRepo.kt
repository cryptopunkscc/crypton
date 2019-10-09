package cc.cryptopunks.crypton.repo

import cc.cryptopunks.crypton.entity.User
import cc.cryptopunks.crypton.entity.UserData
import cc.cryptopunks.crypton.entity.user
import cc.cryptopunks.crypton.entity.userData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class UserRepo(
    private val dao: UserData.Dao
) : User.Repo {

    override suspend fun insert(user: User) =
        dao.insert(user.userData())

    override suspend fun insertIfNeeded(list: List<User>) =
        dao.insertIfNeeded(list.map { it.userData() })

    override suspend fun getById(id: String): User =
        dao.getById(id).user()

    override fun flowListByChatId(chatId: Long): Flow<List<User>> =
        dao.flowListByChatId(chatId).map { it.map(UserData::user) }
}