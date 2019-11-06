package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.entity.Account
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CurrentAccountFlowSelector @Inject constructor(
    private val accountRepo: Account.Repo
) : () -> Flow<Account> by {
    accountRepo.flowList()
        .map { it.firstOrNull(Account::current) }
        .filterNotNull()
        .filter { it.status == Account.Status.Connected }
}