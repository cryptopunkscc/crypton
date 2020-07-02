package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.ChatScope
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.Resource
import cc.cryptopunks.crypton.context.calculateId
import cc.cryptopunks.crypton.context.handle

internal fun ChatScope.handleEnqueueMessage() =
    handle<Chat.Service.EnqueueMessage> {
        chat.createQueuedMessage(text).let { message ->
            log.d("Enqueue message $message")
            messageRepo.insertOrUpdate(message)
        }
    }

private fun Chat.createQueuedMessage(text: String) =
    System.currentTimeMillis().let { timestamp ->
        Message(
            text = text,
            from = Resource(account),
            to = Resource(address),
            status = Message.Status.Queued,
            chat = address,
            timestamp = timestamp
        ).calculateId()
    }
