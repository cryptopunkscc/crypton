package cc.cryptopunks.crypton

import kotlin.coroutines.CoroutineContext

class Handler<A : Action>(
    override val key: Key,
    val handle: Handle<A>,
) : CoroutineContext.Element {
    data class Key(val type: Any) : CoroutineContext.Key<Handler<Action>>
}
