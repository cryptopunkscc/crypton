package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.handle
import cc.cryptopunks.crypton.interactor.flushQueuedMessages

internal fun SessionScope.handleFlushMessageQueue() =
    handle<Chat.Service.FlushQueuedMessages> {
        if (addresses.isEmpty())
            flushQueuedMessages { true } else
            flushQueuedMessages { message: Message -> addresses.contains(message.chat) }
    }
