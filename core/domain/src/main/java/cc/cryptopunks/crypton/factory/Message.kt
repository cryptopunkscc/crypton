package cc.cryptopunks.crypton.factory

import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.Resource
import cc.cryptopunks.crypton.context.calculateId

fun Chat.createMessage(): Message =
    Message(
        from = Resource(account),
        to = Resource(address),
        status = Message.Status.Queued,
        chat = address,
        timestamp = System.currentTimeMillis()
    ).calculateId()
