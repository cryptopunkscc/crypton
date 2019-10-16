package cc.cryptopunks.crypton.util

import kotlin.reflect.KClass

fun KClass<*>.log(message: String) = println("${java.simpleName}: $message")
fun <T: Any>Class<T>.log(message: String) = println("$name: $message")