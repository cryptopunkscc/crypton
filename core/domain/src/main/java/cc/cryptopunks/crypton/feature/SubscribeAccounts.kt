package cc.cryptopunks.crypton.feature

import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.context.Subscribe
import cc.cryptopunks.crypton.feature
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

internal fun subscribeAccounts() = feature(

    handler = { out, _: Subscribe.Accounts ->
        accountRepo.flowList().map { accounts ->
            Account.Many(accounts.toSet())
        }.onEach {
            accounts { it }
        }.collect(out)
    }
)
