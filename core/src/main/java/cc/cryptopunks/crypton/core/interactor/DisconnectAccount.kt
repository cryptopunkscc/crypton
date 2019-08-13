package cc.cryptopunks.crypton.core.interactor

import cc.cryptopunks.crypton.core.entity.Account.Status.Disconnected
import cc.cryptopunks.crypton.core.repository.AccountRepository
import cc.cryptopunks.crypton.core.util.wrap
import io.reactivex.Completable
import javax.inject.Inject

class DisconnectAccount @Inject constructor(
    repository: AccountRepository
) : (Long) -> Completable by { id ->
    repository.copy().run {
        Completable.fromAction {
            load(id)
            disconnect()
            setStatus(Disconnected)
            update()
            clear()
        }.onErrorResumeNext {
            Completable.error(wrap(it))
        }
    }
}