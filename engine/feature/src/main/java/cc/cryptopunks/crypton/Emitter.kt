package cc.cryptopunks.crypton

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flattenMerge

data class Emitter<Scope : CoroutineScope>(
    val type: Class<Scope>,
    val create: Scope.() -> Flow<Any>,
)

inline fun <reified Scope : CoroutineScope> emitter(
    noinline create: Scope.() -> Flow<Any>,
) = Emitter(
    create = create,
    type = Scope::class.java
)

inline fun <reified S : CoroutineScope> CoroutineScope.createEmitters(features: Features) =
    createEmitters(features, S::class.java)

fun CoroutineScope.createEmitters(features: Features, type: Class<*>) = features
    .mapNotNull { feature -> feature.emitter?.takeIf { emitter -> emitter.type == type } }
    .filterIsInstance<Emitter<CoroutineScope>>()
    .map { emitter -> let(emitter.create) }
    .also {
        println("emitters size: ${it.size} for scope $this")
    }
    .asFlow()
    .flattenMerge()
