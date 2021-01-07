package cc.cryptopunks.crypton.create

import cc.cryptopunks.crypton.Emitter
import cc.cryptopunks.crypton.util.mapNotNull
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flattenMerge
import kotlin.coroutines.CoroutineContext

fun CoroutineScope.emission(element: CoroutineContext.Element): Flow<Any> = this
    .getEmitters(element)
    .asFlow()
    .flattenMerge()

private fun CoroutineScope.getEmitters(element: CoroutineContext.Element) = coroutineContext
    .mapNotNull { it as? Emitter }
    .filter { it.type == element }
    .map { emitter -> let(emitter.create) }
