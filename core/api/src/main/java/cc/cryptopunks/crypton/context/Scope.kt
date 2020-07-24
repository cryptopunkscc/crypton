package cc.cryptopunks.crypton.context

import cc.cryptopunks.crypton.Connectable
import cc.cryptopunks.crypton.Scope
import cc.cryptopunks.crypton.Scoped
import cc.cryptopunks.crypton.util.Executors
import cc.cryptopunks.crypton.util.OpenStore
import cc.cryptopunks.crypton.util.Store
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.newSingleThreadContext
import kotlin.reflect.KClass

interface AppScope :
    Scope,
    Executors,
    Sys,
    Repo {

    val mainClass: KClass<*>
    val navigateChatId: Int

    val sessions: SessionScope.Store
    val clipboardStore: Clip.Board.Store
    val connectableBindingsStore: Connectable.Binding.Store
    val lastAccounts: Store<Account.Service.Accounts>
    val rosterItems: Store<Roster.Service.Items>

    val createConnection: Connection.Factory

    fun sessionScope(): SessionScope
    fun sessionScope(address: Address): SessionScope
}

interface SessionScope :
    AppScope,
    SessionRepo,
    Connection,
    Scoped<AppScope> {

    val appScope: AppScope
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

    val chat: Chat
    val pagedMessage: Store<Chat.Service.PagedMessages?>
}
