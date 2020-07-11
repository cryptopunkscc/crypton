package cc.cryptopunks.crypton.repo

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.User
import cc.cryptopunks.crypton.entity.UserData
import cc.cryptopunks.crypton.entity.user
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class UserRepo(
    private val dao: UserData.Dao
) : User.Repo {

    override suspend fun insert(user: Address) =
        dao.insert(UserData(user.id))

    override suspend fun insertIfNeeded(list: List<Address>) =
        dao.insertIfNeeded(list.map { UserData(it.id) })

    override fun flowListByChat(chat: Chat): Flow<List<Address>> =
        dao.flowListByChatId(chat.address.id).map { it.map(UserData::user) }
}
