package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.entity.Account
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AccountListSelector @Inject constructor(
    private val repo: Account.Repo
) : () -> Flow<List<Account>> by {
    repo.flowList()
}