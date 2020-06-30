package cc.cryptopunks.crypton.context

import cc.cryptopunks.crypton.util.Executors
import cc.cryptopunks.crypton.util.IOExecutor
import cc.cryptopunks.crypton.util.MainExecutor
import cc.cryptopunks.crypton.util.ext.invokeOnClose
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.newSingleThreadContext
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KClass

class AppModule(
    val sys: Sys,
    val repo: Repo,
    override val mainClass: KClass<*>,
    override val createConnection: Connection.Factory,
    override val startSessionService: SessionScope.() -> Job,
    override val mainExecutor: MainExecutor,
    override val ioExecutor: IOExecutor
) :
    AppScope,
    Executors,
    Sys by sys,
    Repo by repo {

    override val log = typedLog()
    override val coroutineContext: CoroutineContext =
        log + SupervisorJob() + Dispatchers.IO
    override val sessionStore = SessionScope.Store()
    override val clipboardStore = Clip.Board.Store()
    override val connectableBindingsStore = Connectable.Binding.Store()
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

    override val log = typedLog()
    override val coroutineContext: CoroutineContext =
        log + SupervisorJob() + newSingleThreadContext(address.id)
    override val presenceStore = Presence.Store()
    override fun chatScope(chat: Chat): ChatScope = ChatModule(this, chat)
    override suspend fun chatScope(chatAddress: Address): ChatScope =
        chatScope(chatRepo.get(chatAddress))

    override fun toString(): String = address.toString()

    init {
        invokeOnClose { log.d("Closed $address $it") }
    }
}

class ChatModule(
    sessionScope: SessionScope,
    override val chat: Chat
) :
    SessionScope by sessionScope,
    ChatScope {

    override val log = typedLog()
}
