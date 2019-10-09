package cc.cryptopunks.crypton.feature.account.interactor

import cc.cryptopunks.crypton.entity.Account
import cc.cryptopunks.crypton.entity.Account.Status.Disconnected
import cc.cryptopunks.crypton.feature.account.manager.AccountManager
import cc.cryptopunks.crypton.util.Scope
import kotlinx.coroutines.Job
import javax.inject.Inject

class RegisterAccountInteractor @Inject constructor(
    scope: Scope.UseCase,
    manager: AccountManager,
    connect: ConnectAccountInteractor,
    deleteAccount: DeleteAccountInteractor
) : (Account) -> Job by { account ->
    scope launch {
        manager.copy().run(
            onAccountException = deleteAccount
        ) {
            set(account)
            setStatus(Disconnected)
            register()
            insert()
            connect.suspend(get())
        }
    }
}