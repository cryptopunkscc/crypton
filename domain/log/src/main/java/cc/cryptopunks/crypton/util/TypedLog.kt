package cc.cryptopunks.crypton.util

import kotlin.reflect.KClass

fun Any.typedLog() = TypedLog(this::class)

inline fun <reified T: Any> typedLog() = TypedLog(T::class)

class TypedLog(
    private val type: KClass<*>
){
    fun d(message: Any) = Log.print(
        type,
        Log.Level.Debug,
        message
    )
    fun e(message: Any) = Log.print(
        type,
        Log.Level.Error,
        message
    )
}