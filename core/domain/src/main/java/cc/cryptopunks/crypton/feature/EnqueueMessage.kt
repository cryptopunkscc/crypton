package cc.cryptopunks.crypton.feature

import cc.cryptopunks.crypton.cliv2.command
import cc.cryptopunks.crypton.cliv2.config
import cc.cryptopunks.crypton.cliv2.option
import cc.cryptopunks.crypton.cliv2.optional
import cc.cryptopunks.crypton.cliv2.text
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.factory.createMessage
import cc.cryptopunks.crypton.feature
import cc.cryptopunks.crypton.inContext
import cc.cryptopunks.crypton.util.logger.log

internal fun enqueueMessage() = feature(

    command = command(
        config("account"),
        config("chat"),
        option("-!").optional().copy(description = "Send not encrypted message. Not recommended!"),
        text().copy(name = "message", description = "Message text"),
        name = "-",
        description = "Send a message in chat."
    ) { (account, chat, notEncrypted, message) ->
        Exec.EnqueueMessage(message, notEncrypted.toBoolean().not()).inContext(account, chat)
    },

    handler = { _, arg: Exec.EnqueueMessage ->
        chat.queuedMessage(arg).let { message ->
            log.d { "Enqueue message $message" }
            messageRepo.insertOrUpdate(message)
        }
    }
)

private fun Chat.queuedMessage(enqueueMessage: Exec.EnqueueMessage) =
    createMessage().copy(
        body = enqueueMessage.text,
        encrypted = enqueueMessage.encrypted
    )

