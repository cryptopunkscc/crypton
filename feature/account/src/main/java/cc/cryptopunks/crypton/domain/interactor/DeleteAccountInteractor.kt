package cc.cryptopunks.crypton.domain.interactor

import cc.cryptopunks.crypton.entity.Account
import cc.cryptopunks.crypton.model.AccountModel
import cc.cryptopunks.crypton.util.Scopes
import kotlinx.coroutines.Job
import javax.inject.Inject

class DeleteAccountInteractor @Inject constructor(
    model: AccountModel,
    scope: Scopes.UseCase
) : (Account) -> Job by { account ->
    scope.launch {
        model.copy().run {
            set(account)
            if (get().status == Account.Status.Connected)
                disconnect()
            delete()
        }
    }
}