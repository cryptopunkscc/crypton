package cc.cryptopunks.crypton.core.util.ext

import cc.cryptopunks.kache.core.Kache

inline operator fun <T : Any> Kache<T>.invoke(reduce: T.() -> T?) {
    value.reduce()?.let {
        value = it
    }
}