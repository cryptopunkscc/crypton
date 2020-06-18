package cc.cryptopunks.crypton.context

import cc.cryptopunks.crypton.util.Executors
import cc.cryptopunks.crypton.util.IOExecutor
import cc.cryptopunks.crypton.util.MainExecutor
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KClass

class AppModule(
    val sys: Sys,
    val repo: Repo,
    override val mainClass: KClass<*>,
    override val createConnection: Connection.Factory,
    override val createSessionServices: (SessionScope) -> List<SessionScope.BackgroundService>,
    override val mainExecutor: MainExecutor,
    override val ioExecutor: IOExecutor
) :
    AppScope,
    Executors,
    Sys by sys,
    Repo by repo {

    override val log = typedLog()
    override val coroutineContext: CoroutineContext = SupervisorJob() + Dispatchers.IO
    override val sessionStore = SessionScope.Store()
    override val presenceStore = Presence.Store()
    override val clipboardStore = Clip.Board.Store()
    override val connectableBindingsStore = Connectable.Binding.Store()
    override val navigate = Route.Navigate(routeSys)
    override fun sessionScope(): SessionScope = sessionStore.get().values.first()
    override fun sessionScope(address: Address): SessionScope = sessionStore.get()[address]!!
}

data class SessionModule(
    val appScope: AppScope,
    val connection: Connection,
    val sessionRepo: SessionRepo,
    override val address: Address
) :
    SessionScope,
    AppScope by appScope,
    Net by connection,
    SessionRepo by sessionRepo {

    override val coroutineContext: CoroutineContext = SupervisorJob() + Dispatchers.IO
    override val sessionBackgroundServices by lazy { createSessionServices(this) }
    override fun chatScope(chat: Chat): ChatScope = ChatModule(this, chat)
    override suspend fun chatScope(chatAddress: Address): ChatScope = chatScope(chatRepo.get(chatAddress))
}

class ChatModule(
    sessionScope: SessionScope,
    override val chat: Chat
) :
    SessionScope by sessionScope,
    ChatScope
