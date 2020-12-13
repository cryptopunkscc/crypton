package cc.cryptopunks.crypton.context

import cc.cryptopunks.crypton.asDep
import cc.cryptopunks.crypton.cryptonContext
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.newSingleThreadContext
import kotlin.coroutines.CoroutineContext

fun Repo.context() = cryptonContext(
    accountRepo.asDep<Account.Repo>(),
    clipboardRepo.asDep<Clip.Board.Repo>(),
    createSessionRepo.asDep<SessionRepo.Factory>(),
)

fun SessionRepo.context() = cryptonContext(
    queryContext.asDep<Repo.Context.Query>(),
    transactionContext.asDep<Repo.Context.Transaction>(),
    chatRepo.asDep<Chat.Repo>(),
    messageRepo.asDep<Message.Repo>(),
    rosterRepo.asDep<Roster.Repo>(),
    deviceRepo.asDep<Device.Repo>(),
)

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

interface SessionRepo {
    val queryContext: Repo.Context.Query
    val transactionContext: Repo.Context.Transaction

    val chatRepo: Chat.Repo
    val messageRepo: Message.Repo
    val rosterRepo: Roster.Repo
    val deviceRepo: Device.Repo

    interface Factory : (Address) -> SessionRepo
}
