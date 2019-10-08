package cc.cryptopunks.crypton.feature.account.interactor

import cc.cryptopunks.crypton.entity.Account
import cc.cryptopunks.crypton.feature.account.model.AccountModel
import cc.cryptopunks.crypton.util.Scope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ReconnectAccountsInteractor @Inject constructor(
    private val repo: Account.Repo,
    private val model: AccountModel,
    private val connectAccount: ConnectAccountInteractor,
    private val scope: Scope.UseCase
) : () -> Job {

    override fun invoke() = scope.launch {
        repo.list()
            .asFlow()
            .map { model.copy(it) }
            .filter { it.shouldReconnect() }
            .collect { connectAccount(it.get()).join() }
    }

    private fun AccountModel.shouldReconnect() = listOf(
        Account.Status.Connecting,
        Account.Status.Connected
    ).contains(get().status) && !isInitialized
}