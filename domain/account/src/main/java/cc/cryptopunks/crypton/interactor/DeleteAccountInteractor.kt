package cc.cryptopunks.crypton.interactor

import cc.cryptopunks.crypton.entity.Account
import cc.cryptopunks.crypton.manager.AccountManager
import cc.cryptopunks.crypton.service.Service
import kotlinx.coroutines.Job
import javax.inject.Inject

class DeleteAccountInteractor @Inject constructor(
    manager: AccountManager,
    scope: Service.Scope
) : (Account) -> Job by { account ->
    scope.launch {
        manager.copy().run {
            set(account)
            if (isConnected)
                disconnect()
            delete()
        }
    }
}