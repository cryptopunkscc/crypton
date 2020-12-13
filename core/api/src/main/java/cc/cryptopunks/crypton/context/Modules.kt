package cc.cryptopunks.crypton.context

import cc.cryptopunks.crypton.Connectable
import cc.cryptopunks.crypton.Features
import cc.cryptopunks.crypton.Resolvers
import cc.cryptopunks.crypton.asDep
import cc.cryptopunks.crypton.createHandlers
import cc.cryptopunks.crypton.cryptonContext
import cc.cryptopunks.crypton.util.IOExecutor
import cc.cryptopunks.crypton.util.MainExecutor
import cc.cryptopunks.crypton.util.logger.CoroutineLog
import cc.cryptopunks.crypton.util.logger.log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.newSingleThreadContext

fun RootScope.sessionScope(session: Address): SessionScope =
    requireNotNull(sessions[session]) {
        "Cannot resolve SessionScope for $session\n" +
            "available sessions: ${sessions.get().keys.joinToString("\n")}"
    }

suspend fun SessionScope.chatScope(chat: Address) = ChatModule(this, chatRepo.get(chat))


class RootModule(
    sys: Sys,
    repo: Repo,
    mainClass: Main,
    features: Features,
    resolvers: Resolvers,
    createConnection: Connection.Factory,
    mainExecutor: MainExecutor,
    ioExecutor: IOExecutor,
) : RootScope {

    override val coroutineContext = cryptonContext(

        sys.context(),
        repo.context(),
        features.createHandlers(),
        createConnection.asDep(),

        mainClass,
        features,
        resolvers,
        mainExecutor,
        ioExecutor,
        Chat.NavigationId(),
        ApplicationId(),

        SessionScope.Store(),
        Clip.Board.Store(),
        Connectable.Binding.Store(),
        Account.Store(),
        Roster.Items.Store(),

        SupervisorJob(),
        Dispatchers.IO,
        CoroutineLog.Label(RootScope::class.java.simpleName),
    )

    init {
        coroutineContext[Job]!!.invokeOnCompletion {
            coroutineContext.log.d { "Finish AppModule $this" }
        }
    }
}

class SessionModule(
    rootScope: RootScope,
    connection: Connection,
    sessionRepo: SessionRepo,
    account: Account.Name,
    onClose: (Throwable?) -> Unit = {},
) : SessionScope {

    override val coroutineContext = cryptonContext(
        rootScope.coroutineContext,
        sessionRepo.context(),
        connection.context(),
        rootScope.asDep(),
        account,
        Presence.Store(),
        Address.Subscriptions.Store(),

        SupervisorJob(rootScope.coroutineContext[Job]),
        newSingleThreadContext(account.address.id),
        CoroutineLog.Label(SessionScope::class.java.simpleName),
        CoroutineLog.Scope(account.address.id),
    )

    init {
        deviceNet.setDeviceFingerprintRepo(deviceRepo)
        coroutineContext[Job]!!.invokeOnCompletion {
            onClose(it)
            coroutineContext.log.d { "Finish SessionModule $account ${it.hashCode()} $it" }
        }
    }
}

class ChatModule(
    sessionScope: SessionScope,
    chat: Chat
) : ChatScope {

    override val coroutineContext = cryptonContext(
        sessionScope.coroutineContext,
        sessionScope.asDep(),
        chat,
        Chat.PagedMessages.Store(),

        CoroutineLog.Label(javaClass.simpleName),
        CoroutineLog.Scope(chat.address.id),
    )
}
