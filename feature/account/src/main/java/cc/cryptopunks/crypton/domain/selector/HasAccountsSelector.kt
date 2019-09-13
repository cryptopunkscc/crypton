package cc.cryptopunks.crypton.domain.selector

import cc.cryptopunks.crypton.entity.Account
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HasAccountsSelector @Inject constructor(
    dao: Account.Dao
) : () -> Flow<Boolean> by {
    dao.flowList()
        .map { it.isNotEmpty() }
        .distinctUntilChanged()
}