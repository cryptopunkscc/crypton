package cc.cryptopunks.crypton.repo

import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.User
import cc.cryptopunks.crypton.entity.*
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

    override fun flowListByChat(chat: Chat): Flow<List<User>> =
        dao.flowListByChatId(chat.address.id).map { it.map(UserData::user) }
}