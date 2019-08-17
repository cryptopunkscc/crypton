package cc.cryptopunks.crypton.account.domain.command

import cc.cryptopunks.crypton.account.domain.repository.AccountRepository
import cc.cryptopunks.crypton.account.util.wrap
import cc.cryptopunks.crypton.core.entity.Account.Status.Disconnected
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