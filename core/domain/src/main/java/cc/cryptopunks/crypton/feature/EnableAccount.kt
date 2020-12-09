package cc.cryptopunks.crypton.feature

import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.feature

internal fun enableAccount() = feature(

    handler = { _, (condition): Exec.EnableAccount ->
        accountRepo.run {
            get(account.address).apply {
                if (condition != enabled)
                    update(copy(enabled = condition))
            }
        }
    }
)
