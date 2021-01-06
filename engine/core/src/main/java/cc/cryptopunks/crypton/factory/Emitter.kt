package cc.cryptopunks.crypton.factory

import cc.cryptopunks.crypton.Emitter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

fun emitter(
    type: Any,
    create: CoroutineScope.() -> Flow<Any>,
) = Emitter(
    create = create,
    type = type
)
