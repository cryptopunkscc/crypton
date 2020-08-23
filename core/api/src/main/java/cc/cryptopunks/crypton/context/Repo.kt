package cc.cryptopunks.crypton.context

import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.newSingleThreadContext
import kotlin.coroutines.CoroutineContext

interface Repo {
    val accountRepo: Account.Repo
    val clipboardRepo: Clip.Board.Repo
    val createSessionRepo: SessionRepo.Factory

    sealed class Context(
        private val dispatcher: ExecutorCoroutineDispatcher
    ) : CoroutineContext by dispatcher {
        constructor(name: String) : this(newSingleThreadContext(name))
        class Query : Context("Repo.Query")
        class Transaction : Context("Repo.Transaction")

        val executor get() = dispatcher.executor
    }
}

interface SessionRepo {
    val queryContext: Repo.Context.Query
    val transactionContext: Repo.Context.Transaction

    val chatRepo: Chat.Repo
    val messageRepo: Message.Repo
    val rosterRepo: Roster.Repo
    val deviceRepo: Device.Repo

    interface Factory : (Address) -> SessionRepo
}
