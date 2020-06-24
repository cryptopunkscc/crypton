package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.context.AppScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

fun AppScope.hasAccountsFlow(
): Flow<Account.Service.HasAccounts> = accountRepo.flowList()
    .map { it.isNotEmpty() }
    .distinctUntilChanged()
    .onEach { log.d("Has accounts - $it") }
    .map { Account.Service.HasAccounts(it) }
