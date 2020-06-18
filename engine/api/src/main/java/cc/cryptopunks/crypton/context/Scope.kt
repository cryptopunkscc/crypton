package cc.cryptopunks.crypton.context

import cc.cryptopunks.crypton.util.Executors
import cc.cryptopunks.crypton.util.OpenStore
import cc.cryptopunks.crypton.util.TypedLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.reflect.KClass

interface AppScope :
    CoroutineScope,
    Executors,
    Sys,
    Repo {

    val log: TypedLog
    val mainClass: KClass<*>
    val navigate: Route.Navigate

    val sessionStore: SessionScope.Store
    val presenceStore: Presence.Store
    val clipboardStore: Clip.Board.Store
    val connectableBindingsStore: Connectable.Binding.Store

    val createSessionServices: (SessionScope) -> List<SessionScope.BackgroundService>
    val createConnection: Connection.Factory

    fun sessionScope(): SessionScope
    fun sessionScope(address: Address): SessionScope
}

interface SessionScope :
    AppScope,
    SessionRepo,
    Connection {

    val address: Address
    val scope: CoroutineScope get() = this
    val sessionBackgroundServices: List<SessionScope.BackgroundService>
    fun chatScope(chat: Chat): ChatScope
    suspend fun chatScope(chat: Address): ChatScope

    data class Event internal constructor(
        val session: SessionScope,
        val event: Api.Event
    )

    @Suppress("FunctionName")
    fun Event(event: Api.Event) = Event(
        session = this,
        event = event
    )

    class Scope : CoroutineScope {
        override val coroutineContext = SupervisorJob() + Dispatchers.IO
    }


    class Store : OpenStore<Map<Address, SessionScope>>(emptyMap())

    interface BackgroundService {
        suspend operator fun invoke()
    }
}

interface ChatScope :
    SessionScope {

    val chat: Chat
}
