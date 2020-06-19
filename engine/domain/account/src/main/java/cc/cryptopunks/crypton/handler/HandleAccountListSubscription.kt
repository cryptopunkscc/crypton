package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.context.AppScope
import cc.cryptopunks.crypton.context.handle
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

fun AppScope.handleAccountListSubscription() = handle<Account.Service.SubscribeAccountList> {
    launch {
        accountRepo.flowList().map {
            Account.Service.Accounts(it)
        }.collect(it)
    }
}
