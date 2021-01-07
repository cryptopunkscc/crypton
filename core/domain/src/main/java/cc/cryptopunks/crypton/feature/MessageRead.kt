package cc.cryptopunks.crypton.feature

import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.context.messageRepo
import cc.cryptopunks.crypton.create.handler
import cc.cryptopunks.crypton.feature
import cc.cryptopunks.crypton.logv2.d

internal fun messageRead() = feature(
    handler = handler { _, (messages): Exec.MessagesRead ->
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
