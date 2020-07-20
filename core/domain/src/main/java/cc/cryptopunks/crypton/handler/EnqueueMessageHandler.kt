package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.ChatScope
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.Resource
import cc.cryptopunks.crypton.context.calculateId
import cc.cryptopunks.crypton.handle

internal fun ChatScope.handleEnqueueMessage() =
    handle<Chat.Service.EnqueueMessage> {
        chat.queuedMessage(this).let { message ->
            log.d("Enqueue message $message")
            messageRepo.insertOrUpdate(message)
        }
    }

private fun Chat.queuedMessage(enqueueMessage: Chat.Service.EnqueueMessage) =
    Message(
        text = enqueueMessage.text,
        encrypted = enqueueMessage.encrypted,
        from = Resource(account),
        to = Resource(address),
        status = Message.Status.Queued,
        chat = address,
        timestamp = System.currentTimeMillis()
    ).calculateId()
