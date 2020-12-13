package cc.cryptopunks.crypton.feature

import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.context.messageRepo
import cc.cryptopunks.crypton.feature
import cc.cryptopunks.crypton.util.logger.log

internal fun messageRead() = feature(
    handler = { _, (messages): Exec.MessagesRead ->
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
)
