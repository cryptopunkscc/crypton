package cc.cryptopunks.crypton.core.util

import cc.cryptopunks.crypton.core.entity.Account
import java.util.concurrent.atomic.AtomicReference

internal fun <T> AtomicReference<T>.reduce(f: T.() -> T?) = get().f().also { set(it) }

internal fun <T, R> AtomicReference<T>.get(f: T.() -> R) = get().run { f() }!!

fun AtomicReference<Account>.wrap(throwable: Throwable) =
    if (throwable is Account.Exception) throwable
    else Account.Exception(get(), throwable)
