package cc.cryptopunks.crypton.feature

import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.context.account
import cc.cryptopunks.crypton.context.accountRepo
import cc.cryptopunks.crypton.factory.handler
import cc.cryptopunks.crypton.feature

internal fun enableAccount() = feature(

    handler = handler { _, (condition): Exec.EnableAccount ->
        accountRepo.run {
            get(account.address).apply {
                if (condition != enabled)
                    update(copy(enabled = condition))
            }
        }
    }
)
