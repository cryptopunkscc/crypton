package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.context.handle

internal fun SessionScope.handleDeleteMessage() = handle<Chat.Service.Delete> {
    messageRepo.delete(message)
}
