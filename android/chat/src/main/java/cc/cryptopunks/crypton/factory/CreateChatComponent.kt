package cc.cryptopunks.crypton.factory

import android.os.Bundle
import cc.cryptopunks.crypton.activity.toMap
import cc.cryptopunks.crypton.api.Client
import cc.cryptopunks.crypton.component.ChatComponent
import cc.cryptopunks.crypton.component.DaggerChatComponent
import cc.cryptopunks.crypton.entity.Chat
import cc.cryptopunks.crypton.feature.Route
import cc.cryptopunks.crypton.fragment.CoreFragment
import cc.cryptopunks.crypton.module.ChatModule
import cc.cryptopunks.crypton.module.fragmentComponent
import cc.cryptopunks.crypton.module.viewModelComponent
import javax.inject.Inject

class CreateChatComponent @Inject constructor(
    arguments: Bundle,
    private val coreFragment: CoreFragment,
    private val clientCache: Client.Cache,
    private val chatRepo: Chat.Repo
) {
    private val route = Route.Chat(arguments.toMap())
    private fun getClient() = clientCache[route.accountId]!!
    private suspend fun getChat() = chatRepo.get(route.chatId)

    suspend operator fun invoke() : ChatComponent = DaggerChatComponent.builder()
        .viewModelComponent(coreFragment.viewModelComponent())
        .fragmentComponent(coreFragment.fragmentComponent())
        .client(getClient())
        .chatModule(ChatModule(getChat()))
        .build()
}