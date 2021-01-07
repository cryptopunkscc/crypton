package cc.cryptopunks.crypton.serial

import cc.cryptopunks.crypton.Action
import cc.cryptopunks.crypton.Connector
import cc.cryptopunks.crypton.Scoped
import cc.cryptopunks.crypton.create.inScope
import kotlinx.coroutines.flow.mapNotNull


fun Any.encodeScopedAction(): List<Any> = when (this) {
    is Scoped -> list()
    else -> listOf(this)
}

fun Scoped.list(): List<Any> = listOf<Any>(id) + when (next) {
    is Scoped -> next.list()
    else -> listOf(next)
}

fun Connector.decodeScopedActions(): Connector = copy(
    input = input.mapNotNull(scopedActionDecoder())
)

private fun scopedActionDecoder(): suspend (Any) -> Any? {
    val ids = mutableListOf<String>()
    return { arg ->
        when (arg) {
            is String -> null.also { ids.add(arg) }
            is Action -> arg.inScope(ids).also { ids.clear() }
            else -> throw IllegalArgumentException("Cannot decode $arg")
        }
    }
}
