package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.handle

internal fun handleEnableAccount() = handle { _, (condition): Exec.EnableAccount ->
    accountRepo.run {
        get(address).apply {
            if (condition != enabled)
                update(copy(enabled = condition))
        }
    }
}
