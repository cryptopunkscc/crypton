package cc.cryptopunks.crypton.util.ext

import java.util.concurrent.atomic.AtomicReference


val AtomicReference<*>.isEmpty: Boolean get() = get() == null

inline fun <T, R> AtomicReference<T>.get(f: T.() -> R) = get().run { f() }!!

inline infix fun <T> AtomicReference<T>.reduce(fn: T.() -> T) = apply { synchronized(this) { set(get().fn()) } }