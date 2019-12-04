package cc.cryptopunks.crypton.util

fun Any.typedLog() = TypedLog(this)

inline fun <reified T: Any> typedLog() = TypedLog(T::class)

class TypedLog(
    source: Any
){
    private val label = source.toString()

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
}