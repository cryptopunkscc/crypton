package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.context.Address
import kotlinx.coroutines.flow.Flow

internal class AccountListSelector(
    private val repo: Account.Repo
) : () -> Flow<List<Address>> by {
    repo.flowList()
}
