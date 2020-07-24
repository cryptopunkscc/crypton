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

fun Any.context(vararg ids: String): Any = context(ids.asIterable())

fun Any.context(ids: Iterable<String>): Any =
    ids.reversed().fold(this) { acc, id ->
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
        } else arg.context(ids).also {
            ids.clear()
        }
    }
}
