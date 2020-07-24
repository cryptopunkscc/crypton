package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.handle
import cc.cryptopunks.crypton.interactor.flushQueuedMessages

internal fun handleFlushMessageQueue() =
    handle { _, (addresses): Exec.FlushQueuedMessages ->
        if (addresses.isEmpty())
            flushQueuedMessages { true } else
            flushQueuedMessages { message: Message -> addresses.contains(message.chat) }
    }
