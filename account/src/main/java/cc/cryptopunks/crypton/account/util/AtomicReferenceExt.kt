package cc.cryptopunks.crypton.account.util

import cc.cryptopunks.crypton.core.entity.Account
import java.util.concurrent.atomic.AtomicReference

internal fun AtomicReference<Account>.wrap(throwable: Throwable) =
    if (throwable is Account.Exception) throwable
    else Account.Exception(get(), throwable)
