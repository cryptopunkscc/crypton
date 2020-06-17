package cc.cryptopunks.crypton.mock

import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.User
import kotlinx.coroutines.flow.Flow

class MockUserRepo : User.Repo {

    override suspend fun insert(user: User) {
        TODO("Not yet implemented")
    }

    override suspend fun insertIfNeeded(list: List<User>) {
        TODO("Not yet implemented")
    }

    override fun flowListByChat(chat: Chat): Flow<List<User>> {
        TODO("Not yet implemented")
    }
}
