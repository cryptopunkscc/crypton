package cc.cryptopunks.crypton.repo

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Roster
import cc.cryptopunks.crypton.entity.UserData
import cc.cryptopunks.crypton.entity.user
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RosterRepo(
    private val dao: UserData.Dao
) : Roster.Repo {

    override suspend fun insert(user: Address) =
        dao.insert(UserData(user.id))

    override suspend fun insertIfNeeded(list: List<Address>) =
        dao.insertIfNeeded(list.map { UserData(it.id) })

    override fun flowListByChat(chat: Chat): Flow<List<Address>> =
        dao.flowListByChatId(chat.address.id).map { it.map(UserData::user) }
}
