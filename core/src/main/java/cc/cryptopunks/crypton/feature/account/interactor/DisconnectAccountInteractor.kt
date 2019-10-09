package cc.cryptopunks.crypton.feature.account.interactor

import cc.cryptopunks.crypton.entity.Account
import cc.cryptopunks.crypton.entity.Account.Status.Disconnected
import cc.cryptopunks.crypton.feature.account.manager.AccountManager
import cc.cryptopunks.crypton.util.Scope
import kotlinx.coroutines.Job
import javax.inject.Inject

class DisconnectAccountInteractor @Inject constructor(
    manager: AccountManager,
    scope: Scope.UseCase
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