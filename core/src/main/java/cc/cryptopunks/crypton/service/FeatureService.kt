package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.feature.main.service.UpdateCurrentClientService
import javax.inject.Inject

class FeatureServices @Inject constructor(
    private val updateCurrentClientService: UpdateCurrentClientService,
    private val clientServiceManager: ClientService.Manager
) : () -> Unit by {
    updateCurrentClientService()
    clientServiceManager()
}