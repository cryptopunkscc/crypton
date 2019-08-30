package cc.cryptopunks.crypton.app.presentation.viewmodel

import cc.cryptopunks.crypton.domain.query.HasAccounts
import cc.cryptopunks.crypton.app.service.StartAppService
import cc.cryptopunks.crypton.app.service.StopAppService
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class ToggleAppServiceViewModel @Inject constructor(
    hasAccounts: HasAccounts,
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