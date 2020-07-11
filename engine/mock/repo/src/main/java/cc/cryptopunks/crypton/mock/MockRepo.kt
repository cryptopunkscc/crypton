package cc.cryptopunks.crypton.mock

import cc.cryptopunks.crypton.context.*

class MockRepo : Repo {
    override val accountRepo by lazy { MockAccountRepo() }
    override val clipboardRepo by lazy { MockClipBoardRepo() }
    override val createSessionRepo = MockSessionRepo.Factory()
}

class MockSessionRepo : SessionRepo {
    override val queryContext = Repo.Context.Query()
    override val transactionContext = Repo.Context.Transaction()

    override val chatRepo by lazy { MockChatRepo() }
    override val messageRepo by lazy { MockMessageRepo() }
    override val rosterRepo by lazy { MockRosterRepo() }

    class Factory : SessionRepo.Factory {
        override fun invoke(address: Address) = MockSessionRepo()
    }
}
