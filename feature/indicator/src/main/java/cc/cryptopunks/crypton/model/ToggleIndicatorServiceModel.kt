package cc.cryptopunks.crypton.model

import cc.cryptopunks.crypton.feature.account.selector.HasAccountsSelector
import cc.cryptopunks.crypton.service.StartIndicatorService
import cc.cryptopunks.crypton.service.StopIndicatorService
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class ToggleIndicatorServiceModel @Inject constructor(
    private val hasAccounts: HasAccountsSelector,
    private val startIndicatorService: StartIndicatorService,
    private val stopIndicatorService: StopIndicatorService
) {

    suspend operator fun invoke() = hasAccounts().collect { condition ->
        when (condition) {
            true -> startIndicatorService()
            false -> stopIndicatorService()
        }
    }
}