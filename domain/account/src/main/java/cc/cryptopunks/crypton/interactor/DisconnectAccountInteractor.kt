package cc.cryptopunks.crypton.interactor

import cc.cryptopunks.crypton.entity.Account
import cc.cryptopunks.crypton.entity.Account.Status.Disconnected
import cc.cryptopunks.crypton.manager.AccountManager
import cc.cryptopunks.crypton.service.Service
import kotlinx.coroutines.Job
import javax.inject.Inject

class DisconnectAccountInteractor @Inject constructor(
    manager: AccountManager,
    scope: Service.Scope
) : (Account) -> Job by { id ->
    scope.launch {
        manager.copy().run {
            set(id)
            disconnect()
            setStatus(Disconnected)
            update()
            clear()
        }
    }
}