package cc.cryptopunks.crypton.util

import cc.cryptopunks.crypton.entity.Account
import java.util.concurrent.atomic.AtomicReference

fun AtomicReference<Account>.wrap(throwable: Throwable) =
    if (throwable is Account.Exception) throwable
    else Account.Exception(get(), throwable)
