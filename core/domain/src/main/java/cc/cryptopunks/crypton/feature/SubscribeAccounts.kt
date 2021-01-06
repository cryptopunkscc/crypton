package cc.cryptopunks.crypton.feature

import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.context.Subscribe
import cc.cryptopunks.crypton.context.accountRepo
import cc.cryptopunks.crypton.context.accounts
import cc.cryptopunks.crypton.factory.handler
import cc.cryptopunks.crypton.feature
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

internal fun subscribeAccounts() = feature(

    handler = handler { out, _: Subscribe.Accounts ->
        val accounts = accounts
        accountRepo.flowList().map { list ->
            Account.Many(list.toSet())
        }.onEach {
            accounts { it }
        }.collect(out)
    }
)
