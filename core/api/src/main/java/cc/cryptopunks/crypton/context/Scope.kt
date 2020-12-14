package cc.cryptopunks.crypton.context

import cc.cryptopunks.crypton.Scope
import cc.cryptopunks.crypton.dep
import cc.cryptopunks.crypton.util.OpenStore
import kotlin.coroutines.CoroutineContext

val RootScope.sessions: SessionScope.Store by dep()
val SessionScope.rootScope: RootScope by dep()
val ChatScope.sessionScope: SessionScope by dep()

interface RootScope : Scope {
    data class Module(override val coroutineContext: CoroutineContext) : RootScope
}

interface SessionScope : RootScope {
    data class Module(override val coroutineContext: CoroutineContext) : SessionScope

    class Store : OpenStore<Map<Address, SessionScope>>(emptyMap()) {
        operator fun get(address: Address): SessionScope? = get()[address]
    }
}

interface ChatScope : SessionScope {
    data class Module(override val coroutineContext: CoroutineContext) : ChatScope
}
