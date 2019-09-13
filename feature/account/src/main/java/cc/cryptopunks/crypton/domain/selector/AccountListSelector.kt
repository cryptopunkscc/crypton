package cc.cryptopunks.crypton.domain.selector

import cc.cryptopunks.crypton.entity.Account
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AccountListSelector @Inject constructor(
    private val dao: Account.Dao
) : () -> Flow<List<Account>> by {
    dao.flowList()
}