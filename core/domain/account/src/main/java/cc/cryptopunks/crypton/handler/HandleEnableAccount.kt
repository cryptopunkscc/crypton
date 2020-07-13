package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.context.AppScope
import cc.cryptopunks.crypton.context.handle

internal fun AppScope.handleEnableAccount() =
    handle<Account.Service.Enable> {
        accountRepo.run {
            get(address).apply {
                if (condition != enabled)
                    update(copy(enabled = condition))

            }
        }
    }
