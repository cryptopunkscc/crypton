package cc.cryptopunks.crypton.feature.main.model

import cc.cryptopunks.crypton.api.Client
import cc.cryptopunks.crypton.feature.account.selector.CurrentAccountFlowSelector
import cc.cryptopunks.crypton.util.Scope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UpdateCurrentClientModel @Inject constructor(
    private val currentClient: Client.Current,
    private val clientRepo: Client.Repo,
    private val currentAccountFlow: CurrentAccountFlowSelector,
    private val scope: Scope.UseCase
) : () -> Job by {
    scope.launch {
        currentAccountFlow()
            .map { account -> clientRepo[account] }
            .collect(currentClient.set)
    }
}