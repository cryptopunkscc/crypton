package cc.cryptopunks.crypton.model

import cc.cryptopunks.crypton.domain.selector.HasAccountsSelector
import cc.cryptopunks.crypton.service.StartAppService
import cc.cryptopunks.crypton.service.StopAppService
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class ToggleAppServiceModel @Inject constructor(
    private val hasAccounts: HasAccountsSelector,
    private val startAppService: StartAppService,
    private val stopAppService: StopAppService
) {

    suspend operator fun invoke() = hasAccounts().collect { condition ->
        when (condition) {
            true -> startAppService()
            false -> stopAppService()
        }
    }
}