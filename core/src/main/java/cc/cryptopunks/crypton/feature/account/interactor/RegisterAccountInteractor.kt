package cc.cryptopunks.crypton.feature.account.interactor

import cc.cryptopunks.crypton.entity.Account
import cc.cryptopunks.crypton.entity.Account.Status.Disconnected
import cc.cryptopunks.crypton.entity.onAccountException
import cc.cryptopunks.crypton.feature.account.model.AccountModel
import cc.cryptopunks.crypton.util.Scopes
import kotlinx.coroutines.Job
import javax.inject.Inject

class RegisterAccountInteractor @Inject constructor(
    scope: Scopes.UseCase,
    model: AccountModel,
    connect: ConnectAccountInteractor,
    deleteAccount: DeleteAccountInteractor
) : (Account) -> Job by { account ->
    scope launch {
        model.copy().run {
            set(account)
            setStatus(Disconnected)
            register()
            insert()
            connect(get()).join()
        }
    } onAccountException deleteAccount
}