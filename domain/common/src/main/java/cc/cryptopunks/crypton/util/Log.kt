package cc.cryptopunks.crypton.util

import kotlin.reflect.KClass

inline fun <reified T: Any> log(message: Any) = T::class.log(message)
fun KClass<*>.log(message: Any) = println("${java.simpleName}: $message")
fun <T: Any>Class<T>.log(message: Any) = println("$name: $message")