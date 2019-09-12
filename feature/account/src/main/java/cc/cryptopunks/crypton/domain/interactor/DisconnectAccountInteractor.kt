package cc.cryptopunks.crypton.domain.interactor

import cc.cryptopunks.crypton.domain.repository.AccountRepository
import cc.cryptopunks.crypton.util.wrap
import cc.cryptopunks.crypton.entity.Account.Status.Disconnected
import io.reactivex.Completable
import io.reactivex.Completable.*
import javax.inject.Inject

class DisconnectAccountInteractor @Inject constructor(
    repository: AccountRepository
) : (Long) -> Completable by { id ->
    repository.copy().run {
        fromAction {
            load(id)
            disconnect()
            setStatus(Disconnected)
            update()
            clear()
        }.onErrorResumeNext {
            error(wrap(it))
        }
    }
}