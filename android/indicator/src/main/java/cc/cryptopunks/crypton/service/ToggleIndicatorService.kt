package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.feature.account.selector.HasAccountsSelector
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class ToggleIndicatorService @Inject constructor(
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