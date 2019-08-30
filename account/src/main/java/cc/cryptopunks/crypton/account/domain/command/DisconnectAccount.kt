package cc.cryptopunks.crypton.account.domain.command

import cc.cryptopunks.crypton.account.domain.repository.AccountRepository
import cc.cryptopunks.crypton.account.util.wrap
import cc.cryptopunks.crypton.entity.Account.Status.Disconnected
import io.reactivex.Completable
import io.reactivex.Completable.*
import javax.inject.Inject

class DisconnectAccount @Inject constructor(
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