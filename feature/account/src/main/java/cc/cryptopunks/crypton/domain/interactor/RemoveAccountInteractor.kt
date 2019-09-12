package cc.cryptopunks.crypton.domain.interactor

import cc.cryptopunks.crypton.domain.repository.AccountRepository
import cc.cryptopunks.crypton.util.wrap
import cc.cryptopunks.crypton.entity.Account
import io.reactivex.Completable
import io.reactivex.Completable.*
import javax.inject.Inject

class RemoveAccountInteractor @Inject constructor(
    repository: AccountRepository
) : (Long) -> Completable by { id ->
    repository.copy().run {
        fromAction {
            load(id)
            if (get().status == Account.Status.Connected)
                disconnect()
            remove()
        }.onErrorResumeNext {
            error(wrap(it))
        }
    }
}