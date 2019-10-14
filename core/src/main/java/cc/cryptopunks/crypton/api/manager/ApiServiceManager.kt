package cc.cryptopunks.crypton.api.manager

import cc.cryptopunks.crypton.api.Client
import cc.cryptopunks.crypton.api.isEmpty
import cc.cryptopunks.crypton.api.service.ApiService
import cc.cryptopunks.crypton.api.service.DaggerApiService_Factory
import cc.cryptopunks.crypton.core.Core
import cc.cryptopunks.crypton.service.Service
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ApiServiceManager @Inject constructor(
    private val serviceScope: Service.Scope,
    private val coreComponent: Core.Component,
    private val clientManager: Client.Manager
) : () -> Job {

    override fun invoke() = serviceScope.launch {
        clientManager
            .filterNot { it.isEmpty }
            .map { client -> serviceFactory(client) }
            .map { createApiService -> createApiService() }
            .collect { service -> service() }
    }

    private fun serviceFactory(
        client: Client
    ): ApiService.Factory = DaggerApiService_Factory
        .builder()
        .client(client)
        .component(coreComponent)
        .build()
}