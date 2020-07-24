package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.handle

internal fun handleMessageRead() = handle { _, (messages): Chat.Service.MessagesRead ->
    log.d("Read ${messages.size} messages")
    messageRepo.run {
        insertOrUpdate(
            messages.map { message ->
                message.copy(readAt = System.currentTimeMillis())
            }
        )
        notifyUnread()
    }
}
