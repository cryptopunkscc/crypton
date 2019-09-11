package cc.cryptopunks.crypton.domain.query

import cc.cryptopunks.crypton.entity.Account
import io.reactivex.Flowable
import javax.inject.Inject

class AccountList @Inject constructor(
    private val dao: Account.Dao
) : () -> Flowable<List<Account>> by {
    dao.flowableList()
}