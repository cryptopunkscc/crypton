package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.context.AppScope
import cc.cryptopunks.crypton.handle
import cc.cryptopunks.crypton.util.Store
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

fun AppScope.handleAccountListSubscription(
    last: Store<Account.Service.Accounts>
) =
    handle<Account.Service.SubscribeAccountList> { out ->
        accountRepo.flowList().map {
            Account.Service.Accounts(it)
        }.onEach {
            last { it }
        }.collect(out)
    }

fun AppScope.handleGetAccountList(
    last: Store<Account.Service.Accounts>
) =
    handle<Account.Service.GetAccountList> { out ->
        last.get().takeIf {
            it.list.isNotEmpty()
        }?.let { out(it) }
    }
