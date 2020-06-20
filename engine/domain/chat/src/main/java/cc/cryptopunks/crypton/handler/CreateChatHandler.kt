package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Route
import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.context.createChat
import cc.cryptopunks.crypton.context.handle
import kotlinx.coroutines.launch

internal fun SessionScope.handleCreateChat() =
    handle<Chat.Service.CreateChat> { output ->
        launch {
            try {
                createChat(
                    Chat.Service.CreateChatData(
                        title = chat.id,
                        users = listOf(chat)
                    )
                ).run {
                    Route.Chat().also {
                        it.chatAddress = address.id
                    }.let(navigate)
                }
            } catch (throwable: Throwable) {
                output(throwable)
            }
        }
    }
