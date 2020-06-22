package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.context.handle
import cc.cryptopunks.crypton.interactor.flushQueuedMessages
import kotlinx.coroutines.launch

internal fun SessionScope.handleFlushQueuedMessages() =
    handle<Chat.Service.FlushQueuedMessages> {
        launch {
            if (addresses.isEmpty())
                flushQueuedMessages { true } else
                flushQueuedMessages { message: Message -> addresses.contains(message.chatAddress) }
        }
    }
