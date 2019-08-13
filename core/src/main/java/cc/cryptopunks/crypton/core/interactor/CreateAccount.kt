package cc.cryptopunks.crypton.core.interactor

import cc.cryptopunks.crypton.core.entity.Account
import cc.cryptopunks.crypton.core.entity.Account.Status.Disconnected
import cc.cryptopunks.crypton.core.repository.AccountRepository
import cc.cryptopunks.crypton.core.util.wrap
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class CreateAccount @Inject constructor(
    repository: AccountRepository,
    connect: ConnectAccount
) : (Account) -> Completable by { account ->
    repository.copy().run {
        Single.fromCallable {
            set(account)
            setStatus(Disconnected)
            insert()
            create()
        }.flatMapCompletable {
            connect(id)
        }.onErrorResumeNext {
            Completable.error(wrap(it))
        }
    }
}