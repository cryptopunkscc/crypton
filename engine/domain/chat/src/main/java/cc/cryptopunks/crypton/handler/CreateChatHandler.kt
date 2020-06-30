package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.AppScope
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.asChat
import cc.cryptopunks.crypton.context.createChat
import cc.cryptopunks.crypton.context.handle

internal fun AppScope.handleCreateChat() =
    handle<Chat.Service.CreateChat> { output ->
        try {
            sessionStore.get()[account]!!.createChat(asChat())
            output(Chat.Service.ChatCreated(chat))
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        }
    }
