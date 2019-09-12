package cc.cryptopunks.crypton.domain.interactor

import cc.cryptopunks.crypton.domain.repository.AccountRepository
import cc.cryptopunks.crypton.util.wrap
import cc.cryptopunks.crypton.entity.Account.Status.Connected
import cc.cryptopunks.crypton.entity.Account.Status.Connecting
import io.reactivex.Completable
import javax.inject.Inject

class ConnectAccount @Inject constructor(
    repository: AccountRepository
) : (Long) -> Completable by { id ->
    repository.copy().run {
        Completable.fromAction {
            load(id)
            setStatus(Connecting)
            update()
            login()
            setStatus(Connected)
            update()
        }.onErrorResumeNext {
            Completable.error(wrap(it))
        }
    }
}