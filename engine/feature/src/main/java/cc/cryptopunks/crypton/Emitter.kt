package cc.cryptopunks.crypton

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flattenMerge
import kotlin.coroutines.CoroutineContext

data class Emitter<Scope : CoroutineScope>(
    val type: CoroutineContext.Element,
    val create: Scope.() -> Flow<Any>,
)

fun emitter(
    type: CoroutineContext.Element,
    create: Scope.() -> Flow<Any>,
) = Emitter(
    create = create,
    type = type
)

fun CoroutineScope.createEmitters(
    tag: CoroutineContext.Element,
    features: Features
) =
    createEmitters(features, tag)

fun CoroutineScope.createEmitters(features: Features, type: CoroutineContext.Element) = features
    .mapNotNull { feature -> feature.emitter?.takeIf { emitter -> emitter.type == type } }
    .filterIsInstance<Emitter<CoroutineScope>>()
    .map { emitter -> let(emitter.create) }
    .also {
        println("emitters size: ${it.size} for scope $this")
    }
    .asFlow()
    .flattenMerge()
