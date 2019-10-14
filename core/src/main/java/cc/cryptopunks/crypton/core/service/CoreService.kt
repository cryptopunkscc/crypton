package cc.cryptopunks.crypton.core.service

import cc.cryptopunks.crypton.api.manager.ApiServiceManager
import cc.cryptopunks.crypton.feature.account.service.ReconnectAccountsService
import cc.cryptopunks.crypton.feature.main.service.UpdateCurrentClientService
import javax.inject.Inject

class CoreService @Inject constructor(
    private val reconnectAccountsService: ReconnectAccountsService,
    private val updateCurrentClientService: UpdateCurrentClientService,
    private val apiServiceManager: ApiServiceManager
) : () -> Unit by {
    apiServiceManager()
    updateCurrentClientService()
    reconnectAccountsService()
}