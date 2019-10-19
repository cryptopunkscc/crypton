package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.entity.Account
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HasAccountsSelector @Inject constructor(
    repo: Account.Repo
) : () -> Flow<Boolean> by {
    repo.flowList()
        .map { it.isNotEmpty() }
        .distinctUntilChanged()
}