package cc.cryptopunks.crypton.util

import kotlin.reflect.KClass

inline fun <reified T: Any> log(message: String) = T::class.log(message)
fun KClass<*>.log(message: String) = println("${java.simpleName}: $message")
fun <T: Any>Class<T>.log(message: String) = println("$name: $message")