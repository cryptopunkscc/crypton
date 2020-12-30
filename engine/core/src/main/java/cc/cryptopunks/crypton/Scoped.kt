package cc.cryptopunks.crypton

import kotlinx.coroutines.flow.mapNotNull
import java.lang.IllegalArgumentException

data class Scoped(
    val id: String,
    val next: Action,
) : Action


fun Scoped.action(): Any = when (next) {
    is Scoped -> next.action()
    else -> next
}

fun Scoped.list(): List<Any> = listOf<Any>(id) + when (next) {
    is Scoped -> next.list()
    else -> listOf(next)
}

infix fun Scoped.wrap(
    value: Action,
): Scoped = copy(
    id = id,
    next = when (next) {
        is Scoped -> next.wrap(value)
        else -> value
    }
)

fun Action.inScope(vararg ids: String): Action = inScope(ids.toList())

fun Action.inScope(ids: List<String>): Action =
    ids.foldRight(this) { id, acc ->
        Scoped(
            id = id,
            next = acc
        )
    }

fun Any.encodeScopedAction() = when (this) {
    is Scoped -> list()
    else -> listOf(this)
}

fun Connector.decodeScopedActions() = copy(
    input = input.mapNotNull(scopedActionDecoder())
)

fun scopedActionDecoder(): suspend (Any) -> Any? {
    val ids = mutableListOf<String>()
    return { arg ->
        when (arg) {
            is String -> null.also { ids.add(arg) }
            is Action -> arg.inScope(ids).also { ids.clear() }
            else -> throw IllegalArgumentException("Cannot decode $arg")
        }
    }
}
