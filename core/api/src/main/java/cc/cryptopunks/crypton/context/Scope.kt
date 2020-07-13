package cc.cryptopunks.crypton.context

import cc.cryptopunks.crypton.util.Executors
import cc.cryptopunks.crypton.util.HandlerRegistryFactory
import cc.cryptopunks.crypton.util.OpenStore
import cc.cryptopunks.crypton.util.TypedLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.newSingleThreadContext
import kotlin.reflect.KClass

interface BaseScope :
    CoroutineScope {
    val log: TypedLog
}

interface AppScope :
    BaseScope,
    Executors,
    Sys,
    Repo,
    Connectable {

    val mainClass: KClass<*>

    val sessionStore: SessionScope.Store
    val clipboardStore: Clip.Board.Store
    val connectableBindingsStore: Connectable.Binding.Store

    val startSessionService: SessionScope.() -> Job
    val createConnection: Connection.Factory

    val mainHandlers: HandlerRegistryFactory<AppScope>
    val chatHandlers: HandlerRegistryFactory<ChatScope>

    fun sessionScope(): SessionScope
    fun sessionScope(address: Address): SessionScope
}

interface SessionScope :
    AppScope,
    SessionRepo,
    Connection {

    val address: Address
    val presenceStore: Presence.Store

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
        override val coroutineContext = SupervisorJob() + newSingleThreadContext("Smack")
    }


    class Store : OpenStore<Map<Address, SessionScope>>(emptyMap())
}

interface ChatScope :
    SessionScope {

    val chat: Chat
}
