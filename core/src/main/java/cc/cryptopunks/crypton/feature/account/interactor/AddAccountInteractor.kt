package cc.cryptopunks.crypton.feature.account.interactor

import cc.cryptopunks.crypton.entity.Account
import cc.cryptopunks.crypton.entity.onAccountException
import cc.cryptopunks.crypton.feature.account.manager.AccountManager
import cc.cryptopunks.crypton.util.Scope
import kotlinx.coroutines.Job
import javax.inject.Inject

class AddAccountInteractor @Inject constructor(
    scope: Scope.UseCase,
    manager: AccountManager,
    connect: ConnectAccountInteractor,
    deleteAccount: DeleteAccountInteractor
) : (Account) -> Job by { account ->
    scope.launch {
        manager.copy().run {
            set(account)
            setStatus(Account.Status.Disconnected)
            insert()
            connect(get()).join()
        }
    } onAccountException deleteAccount
}