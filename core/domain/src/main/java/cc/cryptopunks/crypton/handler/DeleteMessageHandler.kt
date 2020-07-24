package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.handle

internal fun handleDeleteMessage() = handle { _, (message): Chat.Service.Delete ->
    messageRepo.delete(message)
}
