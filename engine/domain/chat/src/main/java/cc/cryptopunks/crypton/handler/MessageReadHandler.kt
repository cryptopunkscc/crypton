package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.context.handle
import kotlinx.coroutines.launch

internal fun SessionScope.handleMessageRead() =
    handle<Chat.Service.MessagesRead> {
        scope.launch {
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
    }
