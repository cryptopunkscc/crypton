package cc.cryptopunks.crypton.feature.account.selector

import cc.cryptopunks.crypton.entity.Account
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class NewAccountConnectedSelector @Inject constructor(
    private val repo: Account.Repo
) : () -> Flow<Account> {

    override fun invoke(): Flow<Account> = repo
        .flowList()
        .map { it.filterConnected() }
        .filter { it.isNotEmpty() }
        .map { it.last() }
        .scanReduce { l, r ->  if (r.updateAt > l.updateAt) r else l }
        .distinctUntilChanged()
}

private fun List<Account>.filterConnected() = this
    .filter { it.status == Account.Status.Connected }