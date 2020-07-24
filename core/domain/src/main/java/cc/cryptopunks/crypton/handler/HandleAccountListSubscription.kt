package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.handle
import cc.cryptopunks.crypton.util.Store
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

internal fun handleAccountListSubscription(
    last: Store<Account.Service.Accounts>
) =
    handle { out, _: Account.Service.SubscribeAccountList ->
        accountRepo.flowList().map {
            Account.Service.Accounts(it)
        }.onEach {
            last { it }
        }.collect(out)
    }

internal fun handleGetAccountList(
    last: Store<Account.Service.Accounts>
) =
    handle { out, _: Account.Service.GetAccountList ->
        last.get().takeIf {
            it.list.isNotEmpty()
        }?.let { out(it) }
    }
