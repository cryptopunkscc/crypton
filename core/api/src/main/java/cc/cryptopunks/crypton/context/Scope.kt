package cc.cryptopunks.crypton.context

import cc.cryptopunks.crypton.Connectable
import cc.cryptopunks.crypton.Features
import cc.cryptopunks.crypton.Scope
import cc.cryptopunks.crypton.Scoped
import cc.cryptopunks.crypton.dep
import cc.cryptopunks.crypton.util.Executors
import cc.cryptopunks.crypton.util.OpenStore

val Scope.sessions: SessionScope.Store by dep()
val Scope.rootScope: RootScope by dep()
fun Scope.sessionScope(address: Address): SessionScope =
    requireNotNull(sessions[address]) {
        "Cannot resolve SessionScope for $address\n" +
            "available sessions: ${sessions.get().keys.joinToString("\n")}"
    }

interface RootScope :
    Scope,
    Executors,
    Sys,
    Repo {

    val applicationId: ApplicationId
    val features: Features

    val mainClass: Main
    val navigateChatId: Chat.NavigationId

    val sessions: SessionScope.Store
    val clipboardStore: Clip.Board.Store
    val connectableBindingsStore: Connectable.Binding.Store
    val accounts: Account.Store
    val rosterItems: Roster.Items.Store

    val createConnection: Connection.Factory

    fun sessionScope(address: Address): SessionScope
}

interface SessionScope :
    RootScope,
    SessionRepo,
    Connection,
    Scoped<RootScope> {

    val rootScope: RootScope
    val account: Account.Name
    val presenceStore: Presence.Store
    val subscriptions: Address.Subscriptions.Store

    suspend fun chatScope(chat: Address): ChatScope

    class Store : OpenStore<Map<Address, SessionScope>>(emptyMap()) {
        operator fun get(address: Address): SessionScope? = get()[address]
    }
}


val Scope.sessionScope: SessionScope by dep()

interface ChatScope :
    SessionScope {

    val sessionScope: SessionScope
    val chat: Chat
    val pagedMessages: Chat.PagedMessages.Store
}
