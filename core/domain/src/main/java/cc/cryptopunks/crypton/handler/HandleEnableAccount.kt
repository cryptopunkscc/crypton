package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.handle

internal fun handleEnableAccount() = handle { _, (condition): Account.Service.Enable ->
    accountRepo.run {
        get(address).apply {
            if (condition != enabled)
                update(copy(enabled = condition))
        }
    }
}
