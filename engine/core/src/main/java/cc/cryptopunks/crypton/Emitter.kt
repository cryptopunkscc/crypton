package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.util.CoroutineContextObject
import cc.cryptopunks.crypton.util.Instance
import cc.cryptopunks.crypton.util.mapNotNull
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flattenMerge
import kotlin.coroutines.CoroutineContext

typealias CreateEmission = CoroutineScope.() -> Flow<Any>

data class Emitter(
    val type: Any,
    val create: CreateEmission,
) : CoroutineContextObject

fun emitter(
    type: Any,
    create: CoroutineScope.() -> Flow<Any>,
) = Emitter(
    create = create,
    type = type
)

fun CoroutineScope.createEmission(element: CoroutineContext.Element): Flow<Any> = this
    .getEmitters(element)
    .asFlow()
    .flattenMerge()

fun CoroutineScope.getEmitters(element: CoroutineContext.Element) = coroutineContext
    .mapNotNull { it as? Emitter }
    .filter { it.type == element }
    .map { emitter -> let(emitter.create) }


val CoroutineScope.scopeTag get() = coroutineContext[ScopeTag]

interface ScopeTag : CoroutineContext.Element {
    override val key get() = Key

    companion object Key : CoroutineContext.Key<ScopeTag>
}

data class ScopeElement(val id: String) : Instance
