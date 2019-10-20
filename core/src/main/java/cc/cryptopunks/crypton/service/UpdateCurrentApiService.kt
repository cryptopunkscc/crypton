package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.api.Api
import cc.cryptopunks.crypton.manager.AccountManager
import cc.cryptopunks.crypton.selector.CurrentAccountFlowSelector
import cc.cryptopunks.crypton.util.ext.invokeOnClose
import cc.cryptopunks.crypton.util.log
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UpdateCurrentApiService @Inject constructor(
    private val currentApi: Api.Current,
    private val currentAccountFlow: CurrentAccountFlowSelector,
    private val accountManager: AccountManager,
    private val scope: Service.Scope
) : () -> Job by {
    scope.launch {
        log<UpdateCurrentApiService>("start")
        invokeOnClose { log<UpdateCurrentApiService>("stop") }
        currentAccountFlow()
            .map { account -> accountManager.copy(account) }
            .filter { it.isInitialized }
            .map { it.api() }
            .filter { it.isAuthenticated() }
            .map { it as Api }
            .collect(currentApi.set)
    }
}