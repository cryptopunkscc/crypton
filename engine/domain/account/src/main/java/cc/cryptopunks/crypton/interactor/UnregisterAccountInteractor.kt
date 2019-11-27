package cc.cryptopunks.crypton.interactor

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.manager.AccountManager
import cc.cryptopunks.crypton.service.Service
import kotlinx.coroutines.Job
import javax.inject.Inject

class UnregisterAccountInteractor @Inject constructor(
    manager: AccountManager,
    scope: Service.Scope
) : (Address) -> Job by { account ->
    scope.launch {
        manager.copy(account)
            .unregister()
    }
}