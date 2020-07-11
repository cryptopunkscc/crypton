package cc.cryptopunks.crypton.mock

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Roster
import kotlinx.coroutines.flow.Flow

class MockRosterRepo : Roster.Repo {

    override suspend fun insert(user: Address) {
        TODO("Not yet implemented")
    }

    override suspend fun insertIfNeeded(list: List<Address>) {
        TODO("Not yet implemented")
    }

    override fun flowListByChat(chat: Chat): Flow<List<Address>> {
        TODO("Not yet implemented")
    }
}
