package cc.cryptopunks.crypton.feature.main.service

import cc.cryptopunks.crypton.api.Client
import cc.cryptopunks.crypton.feature.account.selector.CurrentAccountFlowSelector
import cc.cryptopunks.crypton.util.Scope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UpdateCurrentClientService @Inject constructor(
    private val currentClient: Client.Current,
    private val clientManager: Client.Manager,
    private val currentAccountFlow: CurrentAccountFlowSelector,
    private val scope: Scope.UseCase
) : () -> Job by {
    scope.launch {
        currentAccountFlow()
            .map { account -> clientManager[account] }
            .collect(currentClient.set)
    }
}