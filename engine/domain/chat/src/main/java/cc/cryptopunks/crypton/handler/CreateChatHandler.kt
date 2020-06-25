package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.AppScope
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.createChat
import cc.cryptopunks.crypton.context.handle

internal fun AppScope.handleCreateChat() =
    handle<Chat.Service.CreateChat> { output ->
        try {
            sessionStore.get()[account]!!.createChat(
                Chat.Service.CreateChatData(
                    title = chat.id,
                    users = listOf(chat)
                )
            )
            output(Chat.Service.ChatCreated(chat))
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            output(throwable)
        }
    }
