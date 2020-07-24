package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.createChat
import cc.cryptopunks.crypton.handle

internal fun handleCreateChat() = handle { output, (chat): Chat.Service.Create ->
    sessions[chat.account]!!.createChat(chat)
    output(Chat.Service.ChatCreated(chat.address))
}
