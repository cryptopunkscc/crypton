package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.context.*
import cc.cryptopunks.crypton.repo.*

class MockRepo : Repo {
    override val accountRepo by lazy { AccountRepo() }
    override val clipboardRepo by lazy { ClipBoardRepo() }
    override val createSessionRepo = MockSessionRepo.Factory()
}

class MockSessionRepo : SessionRepo {
    override val queryContext = Repo.Context.Query()
    override val transactionContext = Repo.Context.Transaction()

    override val chatRepo by lazy { ChatRepo() }
    override val messageRepo by lazy { MessageRepo() }
    override val userRepo by lazy { UserRepo() }

    class Factory : SessionRepo.Factory {
        override fun invoke(address: Address) = MockSessionRepo()
    }
}
