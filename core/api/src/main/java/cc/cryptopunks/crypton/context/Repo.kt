package cc.cryptopunks.crypton.context

import cc.cryptopunks.crypton.delegate.dep
import cc.cryptopunks.crypton.create.cryptonContext
import cc.cryptopunks.crypton.create.dep
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.newSingleThreadContext
import kotlin.coroutines.CoroutineContext

fun Repo.context() = cryptonContext(
    accountRepo.dep(),
    clipboardRepo.dep(),
    createSessionRepo.dep(),
)

fun SessionRepo.context() = cryptonContext(
    queryContext.dep(),
    transactionContext.dep(),
    chatRepo.dep(),
    messageRepo.dep(),
    rosterRepo.dep(),
    deviceRepo.dep(),
)

val RootScope.createSessionRepo: SessionRepo.Factory by dep()

interface Repo {
    val accountRepo: Account.Repo
    val clipboardRepo: Clip.Board.Repo
    val createSessionRepo: SessionRepo.Factory

    sealed class Context(
        private val dispatcher: ExecutorCoroutineDispatcher,
    ) : CoroutineContext by dispatcher {
        constructor(name: String) : this(newSingleThreadContext(name))

        class Query : Context("Repo.Query")
        class Transaction : Context("Repo.Transaction")

        val executor get() = dispatcher.executor
    }
}

val SessionScope.queryContext: Repo.Context.Query by dep()
val SessionScope.transactionContext: Repo.Context.Transaction by dep()

interface SessionRepo {
    val queryContext: Repo.Context.Query
    val transactionContext: Repo.Context.Transaction

    val chatRepo: Chat.Repo
    val messageRepo: Message.Repo
    val rosterRepo: Roster.Repo
    val deviceRepo: Device.Repo

    interface Factory : (Address) -> SessionRepo
}
