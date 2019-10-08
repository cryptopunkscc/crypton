package cc.cryptopunks.crypton.feature.account.interactor

import cc.cryptopunks.crypton.entity.Account
import cc.cryptopunks.crypton.feature.account.model.AccountModel
import cc.cryptopunks.crypton.util.Scope
import kotlinx.coroutines.Job
import javax.inject.Inject

class DeleteAccountInteractor @Inject constructor(
    model: AccountModel,
    scope: Scope.UseCase
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