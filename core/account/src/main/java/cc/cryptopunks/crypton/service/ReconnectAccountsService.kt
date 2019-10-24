package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.entity.Account
import cc.cryptopunks.crypton.interactor.LoginAccountInteractor
import cc.cryptopunks.crypton.manager.AccountManager
import cc.cryptopunks.crypton.util.ext.invokeOnClose
import cc.cryptopunks.crypton.util.log
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ReconnectAccountsService @Inject constructor(
    private val scope: Service.Scope,
    private val repo: Account.Repo,
    private val manager: AccountManager,
    private val loginAccount: LoginAccountInteractor
) : () -> Job {

    override fun invoke() = scope.launch {
        log("start")
        invokeOnClose { log("stop $it") }
        repo.list()
            .asFlow()
            .map { manager.copy(it) }
            .filter { it.shouldReconnect() }
            .collect {
                log("connecting ${it.get()}")
                loginAccount(it.get()).join()
            }
    }

    private fun AccountManager.shouldReconnect() = listOf(
        Account.Status.Connecting,
        Account.Status.Connected
    ).contains(get().status) && !isInitialized

    private fun log(message: String) = log<ReconnectAccountsService>(message)
}