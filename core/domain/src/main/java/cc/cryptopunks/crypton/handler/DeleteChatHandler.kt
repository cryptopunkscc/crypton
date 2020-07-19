package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.handle

internal fun SessionScope.handleDeleteChat() = handle<Chat.Service.DeleteChat> {
    chatRepo.delete(chats)
}
