package cc.cryptopunks.crypton

data class Context(val id: String = "", val next: Any = Unit)


fun Context.action(): Any = when (next) {
    is Context -> next.action()
    else -> next
}

fun Context.list(): List<Any> = listOf<Any>(id) + when (next) {
    is Context -> next.list()
    else -> listOf(next)
}

infix fun Context.wrap(
    value: Any
): Context = copy(
    id = id,
    next = when (next) {
        is Context -> next.wrap(value)
        else -> value
    }
)

fun Any.inContext(vararg ids: String): Any = inContext(ids.toList())

fun Any.inContext(ids: List<String>): Any =
    ids.foldRight(this) { id, acc ->
        Context(
            id = id,
            next = acc
        )
    }

fun Any.encodeContext() = when (this) {
    is Context -> list()
    else -> listOf(this)
}

fun contextDecoder(): suspend (Any) -> Any? {
    val ids = mutableListOf<String>()
    return { arg ->
        if (arg is String) {
            ids.add(arg)
            null
        } else arg.inContext(ids).also {
            ids.clear()
        }
    }
}
