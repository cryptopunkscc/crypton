package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.handle
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

internal fun handleAccountListSubscription() =
    handle { out, _: Account.Service.SubscribeAccountList ->
        accountRepo.flowList().map {
            Account.Service.Accounts(it)
        }.onEach {
            lastAccounts { it }
        }.collect(out)
    }

internal fun handleGetAccountList() =
    handle { out, _: Account.Service.GetAccountList ->
        lastAccounts.get().takeIf {
            it.list.isNotEmpty()
        }?.out()
    }
