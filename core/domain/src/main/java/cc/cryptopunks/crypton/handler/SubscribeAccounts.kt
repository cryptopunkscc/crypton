package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.context.Get
import cc.cryptopunks.crypton.context.Subscribe
import cc.cryptopunks.crypton.handle
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

internal fun handleAccountsSubscription() =
    handle { out, _: Subscribe.Accounts ->
        accountRepo.flowList().map { accounts ->
            Account.Many(accounts.toSet())
        }.onEach {
            accounts { it }
        }.collect(out)
    }

internal fun handleGetAccountList() =
    handle { out, _: Get.Accounts ->
        Account.Many(accountRepo.addressList().toSet()).out()
    }
