package cc.cryptopunks.crypton.interactor

import cc.cryptopunks.crypton.entity.Address
import cc.cryptopunks.crypton.manager.AccountManager
import cc.cryptopunks.crypton.service.Service
import kotlinx.coroutines.Job
import javax.inject.Inject

class DisconnectAccountInteractor @Inject constructor(
    manager: AccountManager,
    scope: Service.Scope
) : (Address) -> Job by { address ->
    scope.launch {
        manager.copy(address)
            .disconnect()
    }
}