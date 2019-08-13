package cc.cryptopunks.crypton.core.interactor

import cc.cryptopunks.crypton.core.entity.Account.Status.Connected
import cc.cryptopunks.crypton.core.entity.Account.Status.Connecting
import cc.cryptopunks.crypton.core.repository.AccountRepository
import cc.cryptopunks.crypton.core.util.wrap
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