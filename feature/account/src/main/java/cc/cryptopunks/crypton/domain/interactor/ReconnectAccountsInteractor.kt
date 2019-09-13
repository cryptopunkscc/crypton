package cc.cryptopunks.crypton.domain.interactor

import cc.cryptopunks.crypton.domain.repository.AccountRepository
import cc.cryptopunks.crypton.entity.Account
import cc.cryptopunks.crypton.util.Scopes
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ReconnectAccountsInteractor @Inject constructor(
    private val dao: Account.Dao,
    private val repository: AccountRepository,
    private val connectAccount: ConnectAccountInteractor,
    private val scope: Scopes.Feature
) : () -> Job {

    override fun invoke() = scope.launch {
        dao.list()
            .asFlow()
            .map { repository.copy().invoke(it) }
            .filter { it.shouldReconnect() }
            .map { it.id }
            .collect { connectAccount(it).join() }
    }

    private fun AccountRepository.shouldReconnect() = listOf(
        Account.Status.Connecting,
        Account.Status.Connected
    ).contains(get().status) && !isInitialized
}