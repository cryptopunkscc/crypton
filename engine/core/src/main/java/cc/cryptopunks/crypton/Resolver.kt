package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.util.CoroutineContextObject
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

data class Resolver(
    override val key: Key,
    val resolve: suspend CoroutineScope.(Any) -> Any,
) : CoroutineContext.Element {

    data class Key(val type: Any) : CoroutineContext.Key<Resolver>

    object ThrowOnUnknown : CoroutineContextObject
}
