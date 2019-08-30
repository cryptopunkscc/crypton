package cc.cryptopunks.crypton.account.domain.query

import cc.cryptopunks.crypton.entity.Account
import io.reactivex.Observable
import javax.inject.Inject

class AccountList @Inject constructor(
    private val dao: Account.Dao
) : () -> Observable<List<Account>> by {
    dao.observeList()
}