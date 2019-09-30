package cc.cryptopunks.crypton.domain.interactor

import cc.cryptopunks.crypton.entity.Account
import cc.cryptopunks.crypton.entity.Account.Status.Connected
import cc.cryptopunks.crypton.entity.Account.Status.Connecting
import cc.cryptopunks.crypton.model.AccountModel
import cc.cryptopunks.crypton.util.Scopes
import kotlinx.coroutines.Job
import javax.inject.Inject

class ConnectAccountInteractor @Inject constructor(
    scope: Scopes.UseCase,
    model: AccountModel
) : (Account) -> Job by { account ->
    scope.launch {
        model.copy().run {
            set(account)
            setStatus(Connecting)
            update()
            login()
            setStatus(Connected)
            update()
        }
    }
}