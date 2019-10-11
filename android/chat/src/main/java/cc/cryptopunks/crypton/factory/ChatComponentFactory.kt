package cc.cryptopunks.crypton.factory

import android.os.Bundle
import cc.cryptopunks.crypton.util.toMap
import cc.cryptopunks.crypton.component.DaggerChatComponent
import cc.cryptopunks.crypton.component.PresentationComponent
import cc.cryptopunks.crypton.entity.Chat
import cc.cryptopunks.crypton.module.ChatModule
import cc.cryptopunks.crypton.navigation.Route
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class ChatComponentFactory @Inject constructor(
    arguments: Bundle,
    private val component: PresentationComponent,
    private val chatRepo: Chat.Repo
) {
    private val route = Route.Chat(arguments.toMap())
    private suspend fun getChat() = runBlocking(Dispatchers.IO) {
        chatRepo.get(route.chatId)
    }

    suspend operator fun invoke() = DaggerChatComponent
        .builder()
        .presentationComponent(component)
        .chatModule(ChatModule(getChat()))
        .build()!!
}