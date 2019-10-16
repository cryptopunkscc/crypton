package cc.cryptopunks.crypton.feature.account.interactor

import cc.cryptopunks.crypton.entity.Account
import cc.cryptopunks.crypton.entity.Account.Status.Connected
import cc.cryptopunks.crypton.entity.Account.Status.Connecting
import cc.cryptopunks.crypton.feature.account.manager.AccountManager
import cc.cryptopunks.crypton.service.Service
import kotlinx.coroutines.*
import javax.inject.Inject

class ConnectAccountInteractor @Inject constructor(
    private val scope: Service.Scope,
    private val manager: AccountManager
) : (Account) -> Job {

    override fun invoke(account: Account): Job = scope.launch {
        suspend(account)
    }

    internal suspend fun suspend(account: Account) {
        manager.copy().run {
            set(account)
            setStatus(Connecting)
            update()
            login()
            setStatus(Connected)
            update()
        }
    }
}