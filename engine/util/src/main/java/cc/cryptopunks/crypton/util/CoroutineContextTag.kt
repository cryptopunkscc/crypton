package cc.cryptopunks.crypton.util

import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

interface CoroutineContextTag :
    CoroutineContext.Element,
    CoroutineContext.Key<CoroutineContextTag> {
    override val key get() = this
}

operator fun CoroutineScope.contains(key: CoroutineContext.Key<*>): Boolean =
    coroutineContext[key] != null

fun CoroutineContext.elements(): List<CoroutineContext.Element> =
    mutableListOf<CoroutineContext.Element>().also {
        fold(it) { acc, element -> acc += element; acc }
    }
