package cc.cryptopunks.crypton

data class Context(val id: String = "", val any: Any = Unit)


fun Context.action(): Any = when (any) {
    is Context -> any.action()
    else -> any
}

fun Context.list(): List<Any> = listOf<Any>(id) + when (any) {
    is Context -> any.list()
    else -> listOf(any)
}

infix fun Context.wrap(
    value: Any
): Context = copy(
    id = id,
    any = when (any) {
        is Context -> any.wrap(value)
        else -> value
    }
)

fun Any.context(vararg ids: String): Any = context(ids.asIterable())

fun Any.context(ids: Iterable<String>): Any =
    ids.reversed().fold(this) { acc, id ->
        Context(
            id = id,
            any = acc
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
