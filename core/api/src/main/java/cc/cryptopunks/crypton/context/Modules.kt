package cc.cryptopunks.crypton.context

import cc.cryptopunks.crypton.Connectable
import cc.cryptopunks.crypton.Features
import cc.cryptopunks.crypton.HandlerRegistry
import cc.cryptopunks.crypton.Resolvers
import cc.cryptopunks.crypton.createHandlers
import cc.cryptopunks.crypton.util.Executors
import cc.cryptopunks.crypton.util.IOExecutor
import cc.cryptopunks.crypton.util.MainExecutor
import cc.cryptopunks.crypton.util.logger.CoroutineLog
import cc.cryptopunks.crypton.util.logger.log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.newSingleThreadContext
import kotlin.coroutines.CoroutineContext

class RootModule(
    val sys: Sys,
    val repo: Repo,
    override val mainClass: Main,
    override val features: Features,
    override val resolvers: Resolvers,
    override val createConnection: Connection.Factory,
    override val mainExecutor: MainExecutor,
    override val ioExecutor: IOExecutor,
    override val navigateChatId: Chat.NavigationId = Chat.NavigationId(),
    override val applicationId: ApplicationId = ApplicationId(),
) :
    RootScope,
    Executors,
    Sys by sys,
    Repo by repo {

    override val coroutineContext: CoroutineContext = SupervisorJob() +
        Dispatchers.IO +
        CoroutineLog.Label(javaClass.simpleName)

    override val handlers: HandlerRegistry = features.createHandlers()

    override val sessions = SessionScope.Store()
    override val clipboardStore = Clip.Board.Store()
    override val connectableBindingsStore = Connectable.Binding.Store()
    override val accounts = Account.Store()
    override val rosterItems = Roster.Items.Store()

    init {
        coroutineContext[Job]!!.invokeOnCompletion {
            coroutineContext.log.d { "Finish AppModule $this" }
        }
    }

    override fun sessionScope(address: Address): SessionScope =
        sessions[address] ?: throw Exception(
            "Cannot resolve SessionScope for $address\n" +
                "available sessions: ${sessions.get().keys.joinToString("\n")}"
        )
}

class SessionModule(
    override val rootScope: RootScope,
    val connection: Connection,
    val sessionRepo: SessionRepo,
    override val account: Account.Name,
    val onClose: (Throwable?) -> Unit = {},
) :
    SessionScope,
    RootScope by rootScope,
    Connection by connection,
    SessionRepo by sessionRepo {

    override val coroutineContext: CoroutineContext =
        SupervisorJob(rootScope.coroutineContext[Job]) +
            newSingleThreadContext(account.address.id) +
            CoroutineLog.Label(javaClass.simpleName) +
            CoroutineLog.Scope(account.address.id)

    override val presenceStore = Presence.Store()
    override val subscriptions = Address.Subscriptions.Store()

    init {
        deviceNet.setDeviceFingerprintRepo(deviceRepo)
        coroutineContext[Job]!!.invokeOnCompletion {
            onClose(it)
            coroutineContext.log.d { "Finish SessionModule $account ${it.hashCode()} $it" }
        }
    }

    private fun chatScope(chat: Chat): ChatScope = ChatModule(this, chat)
    override suspend fun chatScope(chat: Address): ChatScope = chatScope(chatRepo.get(chat))
}

class ChatModule(
    override val sessionScope: SessionScope,
    override val chat: Chat,
) :
    SessionScope by sessionScope,
    ChatScope {

    override val pagedMessages = Chat.PagedMessages.Store()

    override val coroutineContext = sessionScope.coroutineContext +
        CoroutineLog.Label(javaClass.simpleName) +
        CoroutineLog.Scope(chat.address.id)
}
