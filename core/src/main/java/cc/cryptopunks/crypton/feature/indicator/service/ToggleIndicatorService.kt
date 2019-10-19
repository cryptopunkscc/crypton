package cc.cryptopunks.crypton.feature.indicator.service

import cc.cryptopunks.crypton.entity.Indicator
import cc.cryptopunks.crypton.selector.HasAccountsSelector
import cc.cryptopunks.crypton.service.Service
import cc.cryptopunks.crypton.util.log
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class ToggleIndicatorService @Inject constructor(
    private val scope: Service.Scope,
    private val hasAccounts: HasAccountsSelector,
    private val showIndicator: Indicator.Sys.Show,
    private val hideIndicator: Indicator.Sys.Hide
) : () -> Job by {
    scope.launch {
        log<ToggleIndicatorService>("start")
        hasAccounts().collect { condition ->
            when (condition) {
                true -> showIndicator()
                false -> hideIndicator()
            }
        }
        log<ToggleIndicatorService>("start")
    }
}