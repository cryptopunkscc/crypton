package cc.cryptopunks.crypton

import kotlin.coroutines.CoroutineContext

data class Handler<A : Action>(
    override val key: Key,
    val handle: Handle<A>,
) : CoroutineContext.Element {
    data class Key(val type: Any) : CoroutineContext.Key<Handler<Action>>
}

val NonHandle: Handler<Action> = Handler(Handler.Key(Action::class.java)) { _, _ ->  }
