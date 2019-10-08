package cc.cryptopunks.crypton.factory

import android.os.Bundle
import cc.cryptopunks.crypton.activity.toMap
import cc.cryptopunks.crypton.api.Client
import cc.cryptopunks.crypton.component.DaggerChatComponent
import cc.cryptopunks.crypton.component.PresentationComponent
import cc.cryptopunks.crypton.entity.Chat
import cc.cryptopunks.crypton.feature.Route
import cc.cryptopunks.crypton.module.ChatModule
import javax.inject.Inject

class CreateChatComponent @Inject constructor(
    arguments: Bundle,
    private val component: PresentationComponent,
    private val clientCache: Client.Cache,
    private val chatRepo: Chat.Repo
) {
    private val route = Route.Chat(arguments.toMap())
    private fun getClient() = clientCache[route.accountId]!!
    private suspend fun getChat() = chatRepo.get(route.chatId)

    suspend operator fun invoke() = DaggerChatComponent
        .builder()
        .presentationComponent(component)
        .client(getClient())
        .chatModule(ChatModule(getChat()))
        .build()!!
}