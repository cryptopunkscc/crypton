package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.handle
import cc.cryptopunks.crypton.util.logger.log

internal fun handleMessageRead() = handle { _, (messages): Exec.MessagesRead ->
    log.d { "Read ${messages.size} messages" }
    messageRepo.run {
        insertOrUpdate(
            messages.map { message ->
                message.copy(readAt = System.currentTimeMillis())
            }
        )
        notifyUnread()
    }
}
