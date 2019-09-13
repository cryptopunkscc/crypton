package cc.cryptopunks.crypton.domain.interactor

import cc.cryptopunks.crypton.domain.repository.AccountRepository
import cc.cryptopunks.crypton.entity.Account.Status.Connected
import cc.cryptopunks.crypton.entity.Account.Status.Connecting
import cc.cryptopunks.crypton.util.Scopes
import kotlinx.coroutines.Job
import javax.inject.Inject

class ConnectAccountInteractor @Inject constructor(
    repository: AccountRepository,
    scope: Scopes.Feature
) : (Long) -> Job by { accountId ->
    scope.launch {
        repository.copy().run {
            load(accountId)
            setStatus(Connecting)
            update()
            login()
            setStatus(Connected)
            update()
        }
    }
}