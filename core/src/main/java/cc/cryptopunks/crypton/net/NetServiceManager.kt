package cc.cryptopunks.crypton.net

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

class NetServiceManager @Inject constructor(
    private val serviceScope: Service.Scope,
    private val coreComponent: Core.Component,
    private val currentNet: CurrentNetSelector
) : () -> Job {

    override fun invoke() = serviceScope.launch {
        NetServiceManager::class.log("start")
        invokeOnClose { NetServiceManager::class.log("stop") }
        currentNet()
            .map { api -> serviceFactory(api) }
            .map { createApiService -> createApiService() }
            .collect { service -> service() }
    }

    private fun serviceFactory(
        net: Net
    ): MessageService.Factory = DaggerMessageService_Factory.builder()
        .net(net)
        .net(net as Chat.Net)
        .net(net as Message.Net)
        .repo(coreComponent.repo<Chat.Repo>())
        .repo(coreComponent.repo<Message.Repo>())
        .sys(coreComponent as Message.Sys)
        .presentationManager(coreComponent.presentationManager)
        .build()
}