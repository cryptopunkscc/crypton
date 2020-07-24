package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.handle

internal fun handleDeleteChat() = handle { _, _: Chat.Service.DeleteChat ->
    chatRepo.delete(listOf(chat.address))
}
