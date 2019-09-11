package cc.cryptopunks.crypton.domain.query

import cc.cryptopunks.crypton.entity.Account
import io.reactivex.Flowable
import javax.inject.Inject

class HasAccounts @Inject constructor(
    dao: Account.Dao
) : () -> Flowable<Boolean> by {
    dao.flowableList().map { it.isNotEmpty() }
        .distinctUntilChanged()
}