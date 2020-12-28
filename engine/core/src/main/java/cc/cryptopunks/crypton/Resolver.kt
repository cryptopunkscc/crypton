package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.util.CoroutineContextTag
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

class Resolver(
    type: Any,
    val resolve: CoroutineScope.(Any) -> Any,
) : CoroutineContext.Element {

    override val key = Key(type)
    data class Key(val type: Any) : CoroutineContext.Key<Resolver>

    object ThrowOnUnknown : CoroutineContextTag
}
