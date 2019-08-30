package cc.cryptopunks.crypton.account.domain.query

import cc.cryptopunks.crypton.util.Schedulers
import cc.cryptopunks.crypton.util.runOn
import cc.cryptopunks.crypton.entity.Account
import io.reactivex.Observable
import javax.inject.Inject
import kotlin.math.max

class NewAccountConnected @Inject constructor(
    dao: Account.Dao,
    schedulers: Schedulers
) : () -> Observable<Long> by {
    dao.observeList()
        .skip(1)
        .map { it.getConnectedIds() }
        .filter { it.isNotEmpty() }
        .map { it.last() }
        .scan { t1: Long, t2: Long -> max(t1, t2) }
        .distinctUntilChanged()
        .runOn(schedulers)
}

private fun List<Account>.getConnectedIds() = this
    .filter { it.status == Account.Status.Connected }
    .map(Account::id)