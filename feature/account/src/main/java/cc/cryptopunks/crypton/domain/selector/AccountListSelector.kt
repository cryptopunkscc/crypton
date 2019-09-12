package cc.cryptopunks.crypton.domain.selector

import cc.cryptopunks.crypton.entity.Account
import io.reactivex.Flowable
import javax.inject.Inject

class AccountListSelector @Inject constructor(
    private val dao: Account.Dao
) : () -> Flowable<List<Account>> by {
    dao.flowableList()
}