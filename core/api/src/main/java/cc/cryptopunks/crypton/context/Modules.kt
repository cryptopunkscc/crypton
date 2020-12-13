package cc.cryptopunks.crypton.context

import cc.cryptopunks.crypton.Connectable
import cc.cryptopunks.crypton.Features
import cc.cryptopunks.crypton.HandlerRegistry
import cc.cryptopunks.crypton.Resolvers
import cc.cryptopunks.crypton.asDep
import cc.cryptopunks.crypton.createHandlers
import cc.cryptopunks.crypton.cryptonContext
import cc.cryptopunks.crypton.dep
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
    navigateChatId: Chat.NavigationId = Chat.NavigationId(),
    applicationId: ApplicationId = ApplicationId(),
    handlers: HandlerRegistry = features.createHandlers(),
    sessions: SessionScope.Store = SessionScope.Store(),
    clipboardStore: Clip.Board.Store = Clip.Board.Store(),
    connectableBindingsStore: Connectable.Binding.Store = Connectable.Binding.Store(),
    accounts: Account.Store = Account.Store(),
    rosterItems: Roster.Items.Store = Roster.Items.Store(),
) : RootScope {

    override val coroutineContext = cryptonContext(
        SupervisorJob(),
        Dispatchers.IO,
        CoroutineLog.Label(RootScope::class.java.simpleName),
        sys.context(),
        repo.context(),
        mainClass,
        features,
        resolvers,
        createConnection.asDep<Connection.Factory>(),
        mainExecutor,
        ioExecutor,
        navigateChatId,
        applicationId,
        handlers,
        sessions,
        clipboardStore,
        connectableBindingsStore,
        accounts,
        rosterItems,
    )

    // sys
    override val indicatorSys: Indicator.Sys by dep()
    override val notificationSys: Notification.Sys by dep()
    override val clipboardSys: Clip.Board.Sys by dep()
    override val networkSys: Network.Sys by dep()
    override val deviceSys: Device.Sys by dep()
    override val executeSys: Execute.Sys by dep()
    override val uriSys: URI.Sys by dep()
    override val cryptoSys: Crypto.Sys by dep()
    override val fileSys: File.Sys by dep()

    // repo
    override val accountRepo: Account.Repo by dep()
    override val clipboardRepo: Clip.Board.Repo by dep()
    override val createSessionRepo: SessionRepo.Factory by dep()

    // others
    override val mainClass: Main by dep()
    override val features: Features by dep()
    override val resolvers: Resolvers by dep()
    override val createConnection: Connection.Factory by dep()
    override val mainExecutor: MainExecutor by dep()
    override val ioExecutor: IOExecutor by dep()
    override val navigateChatId: Chat.NavigationId by dep()
    override val applicationId: ApplicationId by dep()
    override val handlers: HandlerRegistry by dep()
    override val sessions: SessionScope.Store by dep()
    override val clipboardStore: Clip.Board.Store by dep()
    override val connectableBindingsStore: Connectable.Binding.Store by dep()
    override val accounts: Account.Store by dep()
    override val rosterItems: Roster.Items.Store by dep()

    init {
        coroutineContext[Job]!!.invokeOnCompletion {
            coroutineContext.log.d { "Finish AppModule $this" }
        }
    }
}

class SessionModule(
    override val rootScope: RootScope,
    connection: Connection,
    sessionRepo: SessionRepo,
    account: Account.Name,
    presenceStore: Presence.Store = Presence.Store(),
    subscriptions: Address.Subscriptions.Store = Address.Subscriptions.Store(),
    onClose: (Throwable?) -> Unit = {},
) :
    RootScope by rootScope,
    SessionScope {

    override val coroutineContext = cryptonContext(
        rootScope.coroutineContext,
        SupervisorJob(rootScope.coroutineContext[Job]),
        newSingleThreadContext(account.address.id),
        CoroutineLog.Label(SessionScope::class.java.simpleName),
        CoroutineLog.Scope(account.address.id),
        connection.context(),
        sessionRepo.context(),
        account,
        presenceStore,
        subscriptions
    )

    // net
    override val net: Net by dep()
    override val accountNet: Account.Net by dep()
    override val messageNet: Message.Net by dep()
    override val chatNet: Chat.Net by dep()
    override val rosterNet: Roster.Net by dep()
    override val deviceNet: Device.Net by dep()
    override val uploadNet: Upload.Net by dep()

    // repo
    override val queryContext: Repo.Context.Query by dep()
    override val transactionContext: Repo.Context.Transaction by dep()
    override val chatRepo: Chat.Repo by dep()
    override val messageRepo: Message.Repo by dep()
    override val rosterRepo: Roster.Repo by dep()
    override val deviceRepo: Device.Repo by dep()

    // others
    override val account: Account.Name by dep()
    override val presenceStore: Presence.Store by dep()
    override val subscriptions: Address.Subscriptions.Store by dep()

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
    chat: Chat,
    pagedMessages: Chat.PagedMessages.Store = Chat.PagedMessages.Store()
) :
    SessionScope by sessionScope,
    ChatScope {

    override val coroutineContext = cryptonContext(
        sessionScope.coroutineContext,
        CoroutineLog.Label(javaClass.simpleName),
        CoroutineLog.Scope(chat.address.id),
        chat,
        pagedMessages,
    )

    override val sessionScope: SessionScope by dep()
    override val chat: Chat by dep()
    override val pagedMessages: Chat.PagedMessages.Store by dep()
}
