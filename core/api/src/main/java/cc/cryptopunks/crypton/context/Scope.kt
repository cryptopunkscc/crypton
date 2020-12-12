package cc.cryptopunks.crypton.context

import cc.cryptopunks.crypton.Connectable
import cc.cryptopunks.crypton.Features
import cc.cryptopunks.crypton.Scope
import cc.cryptopunks.crypton.Scoped
import cc.cryptopunks.crypton.dep
import cc.cryptopunks.crypton.util.Executors
import cc.cryptopunks.crypton.util.OpenStore

val RootScope.sessions: SessionScope.Store by dep()
val SessionScope.rootScope: RootScope by dep()
val ChatScope.sessionScope: SessionScope by dep()

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

    class Store : OpenStore<Map<Address, SessionScope>>(emptyMap()) {
        operator fun get(address: Address): SessionScope? = get()[address]
    }
}

interface ChatScope :
    SessionScope {

    val sessionScope: SessionScope
    val chat: Chat
    val pagedMessages: Chat.PagedMessages.Store
}
