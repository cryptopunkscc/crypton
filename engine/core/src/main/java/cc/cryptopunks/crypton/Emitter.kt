package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.util.CoroutineContextObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

typealias CreateEmission = CoroutineScope.() -> Flow<Any>

data class Emitter(
    val type: Any,
    val create: CreateEmission,
) : CoroutineContextObject
