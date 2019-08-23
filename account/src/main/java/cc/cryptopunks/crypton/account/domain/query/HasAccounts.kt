package cc.cryptopunks.crypton.account.domain.query

import cc.cryptopunks.crypton.core.entity.Account
import io.reactivex.Observable
import javax.inject.Inject

class HasAccounts @Inject constructor(
    dao: Account.Dao
) : () -> Observable<Boolean> by {
    dao.observeList().map { it.isNotEmpty() }
        .distinctUntilChanged()
}