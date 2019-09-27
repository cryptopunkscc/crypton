package cc.cryptopunks.crypton.domain.interactor

import cc.cryptopunks.crypton.repository.AccountRepository
import cc.cryptopunks.crypton.entity.Account
import cc.cryptopunks.crypton.entity.onAccountException
import cc.cryptopunks.crypton.util.Scopes
import kotlinx.coroutines.Job
import javax.inject.Inject

class AddAccountInteractor @Inject constructor(
    scope: Scopes.UseCase,
    repository: AccountRepository,
    connect: ConnectAccountInteractor,
    deleteAccount: DeleteAccountInteractor
) : (Account) -> Job by { account ->
    scope.launch {
        repository.copy().run {
            set(account)
            setStatus(Account.Status.Disconnected)
            insert()
            connect(get()).join()
        }
    } onAccountException deleteAccount
}