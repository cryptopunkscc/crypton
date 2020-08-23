package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.context.RootScope
import cc.cryptopunks.crypton.util.logger.log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

fun RootScope.hasAccountsFlow(
): Flow<Account.HasAccounts> = accountRepo.flowList()
    .map { it.isNotEmpty() }
    .distinctUntilChanged()
//    .onEach { log.d { "Has accounts - $it" } }
    .map { Account.HasAccounts(it) }
