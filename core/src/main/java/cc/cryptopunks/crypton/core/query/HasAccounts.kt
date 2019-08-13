package cc.cryptopunks.crypton.core.query

import cc.cryptopunks.crypton.core.entity.Account
import io.reactivex.Observable
import javax.inject.Inject

class HasAccounts @Inject constructor(
    dao: Account.Dao
) : () -> Observable<Boolean> by {
    dao.observeAll().map { it.isNotEmpty() }
        .distinctUntilChanged()
}