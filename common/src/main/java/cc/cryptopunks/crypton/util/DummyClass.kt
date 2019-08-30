package cc.cryptopunks.crypton.util

import java.lang.reflect.Proxy

inline fun <reified T> createDummyClass() : T = createDummyClass(T::class.java)

fun <T> createDummyClass(type: Class<T>): T = type.cast(
    Proxy.newProxyInstance(
        type.classLoader,
        arrayOf(type)
    ) { _, _, _ -> })!!