package cc.cryptopunks.crypton.feature.main.service

import cc.cryptopunks.crypton.api.Client
import cc.cryptopunks.crypton.manager.AccountManager
import cc.cryptopunks.crypton.selector.CurrentAccountFlowSelector
import cc.cryptopunks.crypton.service.Service
import cc.cryptopunks.crypton.util.ext.invokeOnClose
import cc.cryptopunks.crypton.util.log
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UpdateCurrentClientService @Inject constructor(
    private val currentClient: Client.Current,
    private val currentAccountFlow: CurrentAccountFlowSelector,
    private val accountManager: AccountManager,
    private val scope: Service.Scope
) : () -> Job by {
    scope.launch {
        UpdateCurrentClientService::class.log("start")
        invokeOnClose { UpdateCurrentClientService::class.log("stop ") }
        currentAccountFlow()
            .map { account -> accountManager.copy(account) }
            .filter { it.isInitialized }
            .map { it.api() as Client } // TODO
            .filter { it.isAuthenticated() }
            .collect(currentClient.set)
    }
}