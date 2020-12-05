package cc.cryptopunks.crypton.context

import cc.cryptopunks.crypton.Connectable
import cc.cryptopunks.crypton.Features
import cc.cryptopunks.crypton.Scope
import cc.cryptopunks.crypton.Scoped
import cc.cryptopunks.crypton.util.Executors
import cc.cryptopunks.crypton.util.OpenStore
import cc.cryptopunks.crypton.util.Store
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.newSingleThreadContext
import kotlin.reflect.KClass

interface RootScope :
    Scope,
    Executors,
    Sys,
    Repo {

    val features: Features

    val mainClass: KClass<*>
    val navigateChatId: Int

    val sessions: SessionScope.Store
    val clipboardStore: Clip.Board.Store
    val connectableBindingsStore: Connectable.Binding.Store
    val accounts: Store<Account.Many>
    val rosterItems: Store<Roster.Items>

    val createConnection: Connection.Factory

    fun sessionScope(): SessionScope
    fun sessionScope(address: Address): SessionScope
}

interface SessionScope :
    RootScope,
    SessionRepo,
    Connection,
    Scoped<RootScope> {

    val rootScope: RootScope
    val address: Address
    val presenceStore: Presence.Store
    val subscriptions: OpenStore<Set<Address>>

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


    class Store : OpenStore<Map<Address, SessionScope>>(emptyMap()) {
        operator fun get(address: Address): SessionScope? = get()[address]
    }
}

interface ChatScope :
    SessionScope {

    val sessionScope: SessionScope
    val chat: Chat
    val pagedMessage: Store<Chat.PagedMessages?>
}
