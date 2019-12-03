package cc.cryptopunks.crypton.interactor

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Service
import cc.cryptopunks.crypton.manager.AccountManager
import kotlinx.coroutines.Job
import javax.inject.Inject

class DisconnectAccountInteractor @Inject constructor(
    manager: AccountManager,
    scope: Service.Scope
) : (Address) -> Job by { address ->
    scope.launch {
        manager
            .copy(address = address)
            .interrupt()
    }
}