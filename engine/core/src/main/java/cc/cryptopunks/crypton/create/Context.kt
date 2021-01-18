package cc.cryptopunks.crypton.create

import cc.cryptopunks.crypton.Dependency
import cc.cryptopunks.crypton.util.Instance
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

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

fun build(builder: CoroutineScope.() -> CoroutineContext) = CoroutineContextBuilder(builder)

class CoroutineContextBuilder(val build: CoroutineScope.() -> CoroutineContext) : Instance

fun CoroutineContext.build(): CoroutineContext {
    val holder = HolderScope()
    return fold(EmptyCoroutineContext as CoroutineContext) { context, element ->
        context + when (element) {
            is CoroutineContextBuilder -> {
                holder.coroutineContext = context
                element.build(holder)
            }
            else -> element
        }
    }
}

private class HolderScope(
    override var coroutineContext: CoroutineContext = EmptyCoroutineContext,
) : CoroutineScope
