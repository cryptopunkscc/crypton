package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.ChatScope
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.Resource
import cc.cryptopunks.crypton.context.calculateId
import cc.cryptopunks.crypton.context.handle
import cc.cryptopunks.crypton.util.TypedLog
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.launch

private class SendMessageHandler

internal fun ChatScope.handleSendMessage(
    log: TypedLog = SendMessageHandler().typedLog()
) = handle<Chat.Service.SendMessage> {
    launch {
        chat.createQueuedMessage(text).let { message ->
            log.d("Enqueue message $message")
            messageRepo.insertOrUpdate(message)
        }
    }
}

private fun Chat.createQueuedMessage(text: String) =
    System.currentTimeMillis().let { timestamp ->
        Message(
            text = text,
            from = Resource(account),
            to = Resource(address),
            status = Message.Status.Queued,
            chatAddress = address,
            timestamp = timestamp
        ).calculateId()
    }
