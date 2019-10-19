package cc.cryptopunks.crypton.core.service

import cc.cryptopunks.crypton.api.manager.ApiServiceManager
import cc.cryptopunks.crypton.service.ReconnectAccountsService
import cc.cryptopunks.crypton.feature.indicator.service.ToggleIndicatorService
import cc.cryptopunks.crypton.feature.main.service.UpdateCurrentClientService
import cc.cryptopunks.crypton.util.log
import javax.inject.Inject

class CoreService @Inject constructor(
    private val toggleIndicatorService: ToggleIndicatorService,
    private val reconnectAccountsService: ReconnectAccountsService,
    private val updateCurrentClientService: UpdateCurrentClientService,
    private val apiServiceManager: ApiServiceManager

) : () -> Unit by {
    log<CoreService>("start")
    toggleIndicatorService()
    reconnectAccountsService()
    updateCurrentClientService()
    apiServiceManager()
    log<CoreService>("stop")
}