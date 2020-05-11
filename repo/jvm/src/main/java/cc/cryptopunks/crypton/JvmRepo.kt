package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.context.*
import cc.cryptopunks.crypton.repo.*

class JvmRepo : Repo {

    override val accountRepo: Account.Repo by lazy { AccountRepo() }

    override val clipboardRepo: Clip.Board.Repo by lazy { ClipBoardRepo() }
    override val createSessionRepo: SessionRepo.Factory = JvmSessionRepo.Factory()
}

class JvmSessionRepo : SessionRepo {
    override val queryContext = Repo.Context.Query()
    override val transactionContext = Repo.Context.Transaction()

    override val chatRepo: Chat.Repo by lazy { ChatRepo() }
    override val messageRepo: Message.Repo by lazy { MessageRepo() }
    override val userRepo: User.Repo by lazy { UserRepo() }

    class Factory : SessionRepo.Factory {
        override fun invoke(address: Address): SessionRepo = JvmSessionRepo()
    }
}
