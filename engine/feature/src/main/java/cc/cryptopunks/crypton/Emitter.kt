package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.util.CoroutineContextObject
import cc.cryptopunks.crypton.util.mapNotNull
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flattenMerge
import kotlin.coroutines.CoroutineContext

data class Emitter(
    val type: CoroutineContext.Element,
    val create: CoroutineScope.() -> Flow<Any>,
) : CoroutineContextObject

fun emitter(
    type: CoroutineContext.Element,
    create: CoroutineScope.() -> Flow<Any>,
) = Emitter(
    create = create,
    type = type
)

fun CoroutineScope.createEmitters(type: CoroutineContext.Element) = coroutineContext
    .mapNotNull { it as? Emitter }
    .filter { it.type == type }
    .map { emitter -> let(emitter.create) }
    .also { println("emitters size: ${it.size} for scope $this") }
    .asFlow()
    .flattenMerge()


val CoroutineScope.scopeTag get() = coroutineContext[ScopeTag]

interface ScopeTag : CoroutineContext.Element {
    override val key get() = Key
    companion object Key : CoroutineContext.Key<ScopeTag>
}
