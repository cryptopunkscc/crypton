package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.context.Address
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AccountListSelector @Inject constructor(
    private val repo: Account.Repo
) : () -> Flow<List<Address>> by {
    repo.flowList()
}