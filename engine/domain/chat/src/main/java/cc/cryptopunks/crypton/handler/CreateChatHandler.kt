package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.AppScope
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.createChat
import cc.cryptopunks.crypton.context.handle

internal fun AppScope.handleCreateChat() =
//    handle<Chat.Service.CreateChat> { output ->
    handle<Chat.Service.Create> { output ->
        try {
            sessionStore.get().getValue(chat.account).createChat(chat)
            output(Chat.Service.ChatCreated(chat.address))
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        }
    }
