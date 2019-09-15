package cc.cryptopunks.crypton.domain.interactor

import cc.cryptopunks.crypton.domain.repository.AccountRepository
import cc.cryptopunks.crypton.entity.Account
import cc.cryptopunks.crypton.util.Scopes
import kotlinx.coroutines.Job
import javax.inject.Inject

class AddAccountInteractor @Inject constructor(
    repository: AccountRepository,
    connect: ConnectAccountInteractor,
    scope: Scopes.UseCase
) : (Account) -> Job by { account ->
    scope.launch {
        repository.copy().run {
            set(account)
            setStatus(Account.Status.Disconnected)
            insert()
            connect(get().id).join()
        }
    }
}