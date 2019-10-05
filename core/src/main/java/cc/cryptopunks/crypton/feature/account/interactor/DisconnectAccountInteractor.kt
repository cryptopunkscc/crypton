package cc.cryptopunks.crypton.feature.account.interactor

import cc.cryptopunks.crypton.entity.Account
import cc.cryptopunks.crypton.entity.Account.Status.Disconnected
import cc.cryptopunks.crypton.feature.account.model.AccountModel
import cc.cryptopunks.crypton.util.Scopes
import kotlinx.coroutines.Job
import javax.inject.Inject

class DisconnectAccountInteractor @Inject constructor(
    model: AccountModel,
    scope: Scopes.UseCase
) : (Account) -> Job by { id ->
    scope.launch {
        model.copy().run {
            set(id)
            disconnect()
            setStatus(Disconnected)
            update()
            clear()
        }
    }
}