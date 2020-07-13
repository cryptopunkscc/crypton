package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.AppScope
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.createChat
import cc.cryptopunks.crypton.handle

internal fun AppScope.handleCreateChat() =
    handle<Chat.Service.Create> { output ->
        sessionStore.get().getValue(chat.account).createChat(chat)
        output(Chat.Service.ChatCreated(chat.address))
    }
