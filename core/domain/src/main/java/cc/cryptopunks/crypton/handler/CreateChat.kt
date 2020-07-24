package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.context.createChat
import cc.cryptopunks.crypton.handle

internal fun handleCreateChat() = handle { output, (chat): Exec.CreateChat ->
    sessions[chat.account]!!.createChat(chat)
    output(Chat.Service.ChatCreated(chat.address))
}
