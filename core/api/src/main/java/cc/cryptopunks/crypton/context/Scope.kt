package cc.cryptopunks.crypton.context

import cc.cryptopunks.crypton.Scope
import cc.cryptopunks.crypton.dep
import cc.cryptopunks.crypton.util.OpenStore

val RootScope.sessions: SessionScope.Store by dep()
val SessionScope.rootScope: RootScope by dep()
val ChatScope.sessionScope: SessionScope by dep()

interface RootScope :
    Scope

interface SessionScope :
    RootScope {

    class Store : OpenStore<Map<Address, SessionScope>>(emptyMap()) {
        operator fun get(address: Address): SessionScope? = get()[address]
    }
}

interface ChatScope :
    SessionScope
