package cc.cryptopunks.crypton.presentation.viewmodel

import cc.cryptopunks.crypton.domain.selector.HasAccountsSelector
import cc.cryptopunks.crypton.service.StartAppService
import cc.cryptopunks.crypton.service.StopAppService
import cc.cryptopunks.crypton.util.Scopes
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class ToggleAppServiceViewModel @Inject constructor(
    hasAccounts: HasAccountsSelector,
    startAppService: StartAppService,
    stopAppService: StopAppService,
    scope: Scopes.ViewModel
) : () -> Job by {
    scope.launch {
        hasAccounts().collect { condition ->
            when (condition) {
                true -> startAppService()
                false -> stopAppService()
            }
        }
    }
}