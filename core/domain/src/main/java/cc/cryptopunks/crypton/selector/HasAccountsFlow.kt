package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.context.RootScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

fun RootScope.hasAccountsFlow(
): Flow<Account.HasAccounts> = accountRepo.flowList()
    .map { it.isNotEmpty() }
    .distinctUntilChanged()
    .map { Account.HasAccounts(it) }
