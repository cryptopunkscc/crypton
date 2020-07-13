package cc.cryptopunks.crypton.util

import kotlin.coroutines.CoroutineContext

fun Any.typedLog() = TypedLog(this)

inline fun <reified T : Any> typedLog() = TypedLog(T::class)

class TypedLog(
    source: Any
) : CoroutineContext.Element {
    val label = source.toString()

    fun d(message: Any) = Log.print(
        label,
        Log.Level.Debug,
        message
    )

    fun e(message: Any) = Log.print(
        label,
        Log.Level.Error,
        message
    )

    companion object : CoroutineContext.Key<TypedLog>

    override val key: CoroutineContext.Key<*> get() = TypedLog
}
