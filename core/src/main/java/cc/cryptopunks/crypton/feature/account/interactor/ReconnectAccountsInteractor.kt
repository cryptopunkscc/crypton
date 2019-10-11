package cc.cryptopunks.crypton.feature.account.interactor

import cc.cryptopunks.crypton.entity.Account
import cc.cryptopunks.crypton.feature.account.manager.AccountManager
import cc.cryptopunks.crypton.service.Service
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ReconnectAccountsInteractor @Inject constructor(
    private val repo: Account.Repo,
    private val manager: AccountManager,
    private val connectAccount: ConnectAccountInteractor,
    private val scope: Service.Scope
) : () -> Job {

    override fun invoke() = scope.launch {
        repo.list()
            .asFlow()
            .map { manager.copy(it) }
            .filter { it.shouldReconnect() }
            .collect { connectAccount(it.get()).join() }
    }

    private fun AccountManager.shouldReconnect() = listOf(
        Account.Status.Connecting,
        Account.Status.Connected
    ).contains(get().status) && !isInitialized
}