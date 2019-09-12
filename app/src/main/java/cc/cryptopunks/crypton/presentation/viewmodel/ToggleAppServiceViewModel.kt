package cc.cryptopunks.crypton.presentation.viewmodel

import cc.cryptopunks.crypton.domain.selector.HasAccountsSelector
import cc.cryptopunks.crypton.service.StartAppService
import cc.cryptopunks.crypton.service.StopAppService
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class ToggleAppServiceViewModel @Inject constructor(
    hasAccounts: HasAccountsSelector,
    startAppService: StartAppService,
    stopAppService: StopAppService
) : () -> Disposable by {
    hasAccounts().subscribe { condition ->
        when (condition) {
            true -> startAppService()
            false -> stopAppService()
        }
    }
}