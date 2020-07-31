package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.Resource
import cc.cryptopunks.crypton.context.calculateId
import cc.cryptopunks.crypton.handle
import cc.cryptopunks.crypton.util.logger.log

internal fun handleEnqueueMessage() = handle { _, arg: Exec.EnqueueMessage ->
    chat.queuedMessage(arg).let { message ->
        log.d { "Enqueue message $message" }
        messageRepo.insertOrUpdate(message)
    }
}

private fun Chat.queuedMessage(enqueueMessage: Exec.EnqueueMessage) =
    Message(
        text = enqueueMessage.text,
        encrypted = enqueueMessage.encrypted,
        from = Resource(account),
        to = Resource(address),
        status = Message.Status.Queued,
        chat = address,
        timestamp = System.currentTimeMillis()
    ).calculateId()
