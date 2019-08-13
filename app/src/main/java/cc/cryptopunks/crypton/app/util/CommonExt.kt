package cc.cryptopunks.crypton.app.util

import cc.cryptopunks.kache.core.Kache
import java.util.concurrent.atomic.AtomicReference

val AtomicReference<*>.isEmpty: Boolean get() = get() == null

inline infix fun <T> AtomicReference<T>.reduce(fn: T.() -> T) = synchronized(this) { set(get().fn()) }

inline operator fun <T : Any> Kache<T>.invoke(reduce: T.() -> T?) {
    value.reduce()?.let {
        value = it
    }
}