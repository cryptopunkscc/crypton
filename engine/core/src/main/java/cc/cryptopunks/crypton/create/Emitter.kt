package cc.cryptopunks.crypton.create

import cc.cryptopunks.crypton.Emitter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

fun emitter(
    type: Any = Unit,
    create: CoroutineScope.() -> Flow<Any>,
) = Emitter(
    create = create,
    type = type
)
