package cc.cryptopunks.crypton.interactor

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Service
import cc.cryptopunks.crypton.manager.AccountManager
import cc.cryptopunks.crypton.util.Log
import cc.cryptopunks.crypton.util.d
import kotlinx.coroutines.Job
import javax.inject.Inject

class LoginAccountInteractor @Inject constructor(
    private val scope: Service.Scope,
    private val manager: AccountManager
) : (Address) -> Job {

    override fun invoke(address: Address): Job = scope.launch {
        invoke(manager.copy(address = address))
    }

    internal operator fun invoke(manager: AccountManager) {
        Log.d<LoginAccountInteractor>("login")
        manager.run {
            if (!isConnected) connect()
            if (!isAuthenticated) login()
            initOmemo()
        }
    }
}