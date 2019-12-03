package cc.cryptopunks.crypton.context

import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.newSingleThreadContext
import kotlin.coroutines.CoroutineContext

interface Repo {
    val queryContext: Context.Query
    val transactionContext: Context.Transaction

    val accountRepo: Account.Repo
    val chatRepo: Chat.Repo
    val messageRepo: Message.Repo
    val userRepo: User.Repo
    val clipboardRepo: Clip.Board.Repo

    sealed class Context(
        private val dispatcher: ExecutorCoroutineDispatcher
    ) : CoroutineContext by dispatcher {

        constructor(name: String) : this(newSingleThreadContext(name))

        val executor get() = dispatcher.executor

        class Query : Context("Repo.Query")
        class Transaction : Context("Repo.Transaction")
    }
}