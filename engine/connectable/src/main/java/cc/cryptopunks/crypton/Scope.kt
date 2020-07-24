
package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.util.TypedLog
import kotlinx.coroutines.CoroutineScope

interface Scope : CoroutineScope {
    val log: TypedLog
    val handlers: HandlerRegistry
    suspend infix fun resolve(context: Context): Pair<Scope, Any> = this to Unit
}

interface Scoped<S : CoroutineScope>

@Suppress("UNCHECKED_CAST")
internal fun Scope.handlerFor(any: Any): Handle<Any, Any>? = handlers[any::class]
