package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.context.Account
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

internal class HasAccountsSelector(
    repo: Account.Repo
) : () -> Flow<Boolean> by {
    repo.flowList()
        .map { it.isNotEmpty() }
        .distinctUntilChanged()
}
