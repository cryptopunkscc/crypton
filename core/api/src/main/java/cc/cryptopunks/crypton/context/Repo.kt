package cc.cryptopunks.crypton.context

import cc.cryptopunks.crypton.asDep
import cc.cryptopunks.crypton.cryptonContext
import cc.cryptopunks.crypton.dep
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.newSingleThreadContext
import kotlin.coroutines.CoroutineContext

fun Repo.context() = cryptonContext(
    accountRepo.asDep(),
    clipboardRepo.asDep(),
    createSessionRepo.asDep(),
)

fun SessionRepo.context() = cryptonContext(
    queryContext.asDep(),
    transactionContext.asDep(),
    chatRepo.asDep(),
    messageRepo.asDep(),
    rosterRepo.asDep(),
    deviceRepo.asDep(),
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
