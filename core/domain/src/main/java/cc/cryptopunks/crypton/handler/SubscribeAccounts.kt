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
        accountRepo.flowList().map {
            Account.Service.Accounts(it)
        }.onEach {
            lastAccounts { it }
        }.collect(out)
    }

internal fun handleGetAccountList() =
    handle { out, _: Get.Accounts ->
        lastAccounts.get().takeIf {
            it.list.isNotEmpty()
        }?.out()
    }
