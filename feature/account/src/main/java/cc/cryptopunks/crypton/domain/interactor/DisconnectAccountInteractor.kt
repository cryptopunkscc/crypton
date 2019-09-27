package cc.cryptopunks.crypton.domain.interactor

import cc.cryptopunks.crypton.repository.AccountRepository
import cc.cryptopunks.crypton.entity.Account
import cc.cryptopunks.crypton.entity.Account.Status.Disconnected
import cc.cryptopunks.crypton.util.Scopes
import kotlinx.coroutines.Job
import javax.inject.Inject

class DisconnectAccountInteractor @Inject constructor(
    repository: AccountRepository,
    scope: Scopes.UseCase
) : (Account) -> Job by { id ->
    scope.launch {
        repository.copy().run {
            set(id)
            disconnect()
            setStatus(Disconnected)
            update()
            clear()
        }
    }
}