package cc.cryptopunks.crypton.create

import cc.cryptopunks.crypton.Emitter
import cc.cryptopunks.crypton.util.mapNotNull
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flattenMerge

fun CoroutineScope.emission(element: Any = Unit): Flow<Any> = this
    .getEmitters(element)
    .asFlow()
    .flattenMerge()

private fun CoroutineScope.getEmitters(element: Any = Unit) = coroutineContext
    .mapNotNull { it as? Emitter }
    .filter { it.type == element }
    .map { emitter -> let(emitter.create) }
