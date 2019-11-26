package cc.cryptopunks.crypton.interactor

import cc.cryptopunks.crypton.entity.Account
import cc.cryptopunks.crypton.manager.AccountManager
import cc.cryptopunks.crypton.service.Service
import kotlinx.coroutines.Job
import javax.inject.Inject

class LoginAccountInteractor @Inject constructor(
    private val scope: Service.Scope,
    private val manager: AccountManager
) : (Account) -> Job {

    override fun invoke(account: Account): Job = scope.launch {
        suspend(account)
    }

    internal suspend fun suspend(account: Account) {
        manager.copy().run {
            set(account)
            if (!isConnected) connect()
            if (!isAuthenticated) login()
            initOmemo()
        }
    }
}