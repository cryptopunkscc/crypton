package cc.cryptopunks.crypton.domain.interactor

import cc.cryptopunks.crypton.entity.Account
import cc.cryptopunks.crypton.entity.onAccountException
import cc.cryptopunks.crypton.model.AccountModel
import cc.cryptopunks.crypton.util.Scopes
import kotlinx.coroutines.Job
import javax.inject.Inject

class AddAccountInteractor @Inject constructor(
    scope: Scopes.UseCase,
    model: AccountModel,
    connect: ConnectAccountInteractor,
    deleteAccount: DeleteAccountInteractor
) : (Account) -> Job by { account ->
    scope.launch {
        model.copy().run {
            set(account)
            setStatus(Account.Status.Disconnected)
            insert()
            connect(get()).join()
        }
    } onAccountException deleteAccount
}