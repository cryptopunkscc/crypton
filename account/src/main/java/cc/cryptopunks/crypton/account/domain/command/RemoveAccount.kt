package cc.cryptopunks.crypton.account.domain.command

import cc.cryptopunks.crypton.account.domain.repository.AccountRepository
import cc.cryptopunks.crypton.account.util.wrap
import cc.cryptopunks.crypton.entity.Account
import io.reactivex.Completable
import io.reactivex.Completable.*
import javax.inject.Inject

class RemoveAccount @Inject constructor(
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