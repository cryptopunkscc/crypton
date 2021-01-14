package cc.cryptopunks.crypton.create

import cc.cryptopunks.crypton.Dependency
import kotlin.coroutines.CoroutineContext

fun cryptonContext(
    vararg elements: Any,
): CoroutineContext = cryptonContext(elements.asList())

fun cryptonContext(
    elements: List<Any>,
): CoroutineContext = elements
    .map(Any::asCoroutineContext)
    .reduce(CoroutineContext::plus)

private fun Any.asCoroutineContext():
    CoroutineContext = this
    as? CoroutineContext
    ?: Dependency(Dependency.Key(javaClass)) { this }
