package cc.cryptopunks.crypton

import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

val CoroutineScope.scopeTag get() = coroutineContext[ScopeTag]

interface ScopeTag : CoroutineContext.Element {
    override val key get() = Key
    companion object Key : CoroutineContext.Key<ScopeTag>
}
