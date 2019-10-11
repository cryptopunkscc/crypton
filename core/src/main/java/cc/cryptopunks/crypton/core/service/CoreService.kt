package cc.cryptopunks.crypton.core.service

import cc.cryptopunks.crypton.api.service.ApiService
import cc.cryptopunks.crypton.feature.main.service.UpdateCurrentClientService
import javax.inject.Inject

class CoreService @Inject constructor(
    private val updateCurrentClientService: UpdateCurrentClientService,
    private val apiServiceManager: ApiService.Manager
) : () -> Unit by {
    updateCurrentClientService()
    apiServiceManager()
}