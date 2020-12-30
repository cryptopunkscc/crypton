package cc.cryptopunks.crypton.context

import cc.cryptopunks.crypton.ScopeTag
import cc.cryptopunks.crypton.dep
import cc.cryptopunks.crypton.util.OpenStore
import kotlinx.coroutines.CoroutineScope

val RootScope.sessions: SessionStore by dep()
val SessionScope.rootScope: RootScope by dep(RootScopeTag)
val ChatScope.sessionScope: SessionScope by dep(SessionScopeTag)

typealias RootScope = CoroutineScope

typealias SessionScope = RootScope

class SessionStore : OpenStore<Map<Address, SessionScope>>(emptyMap()) {
    operator fun get(address: Address): SessionScope? = get()[address]
}

typealias ChatScope = SessionScope

object RootScopeTag : ScopeTag
object SessionScopeTag : ScopeTag
object ChatScopeTag : ScopeTag
