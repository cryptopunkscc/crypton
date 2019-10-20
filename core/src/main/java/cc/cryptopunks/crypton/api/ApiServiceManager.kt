package cc.cryptopunks.crypton.api

import cc.cryptopunks.crypton.core.Core
import cc.cryptopunks.crypton.entity.Chat
import cc.cryptopunks.crypton.entity.Message
import cc.cryptopunks.crypton.repo.repo
import cc.cryptopunks.crypton.service.DaggerMessageService_Factory
import cc.cryptopunks.crypton.service.MessageService
import cc.cryptopunks.crypton.service.Service
import cc.cryptopunks.crypton.util.ext.invokeOnClose
import cc.cryptopunks.crypton.util.log
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ApiServiceManager @Inject constructor(
    private val serviceScope: Service.Scope,
    private val coreComponent: Core.Component,
    private val currentApi: CurrentApiSelector
) : () -> Job {

    override fun invoke() = serviceScope.launch {
        ApiServiceManager::class.log("start")
        invokeOnClose { ApiServiceManager::class.log("stop") }
        currentApi()
            .map { api -> serviceFactory(api) }
            .map { createApiService -> createApiService() }
            .collect { service -> service() }
    }

    private fun serviceFactory(
        api: Api
    ): MessageService.Factory = DaggerMessageService_Factory.builder()
        .api(api)
        .api(api as Chat.Api)
        .api(api as Message.Api)
        .repo(coreComponent.repo<Chat.Repo>())
        .repo(coreComponent.repo<Message.Repo>())
        .sys(coreComponent as Message.Sys)
        .presentationManager(coreComponent.presentationManager)
        .build()
}