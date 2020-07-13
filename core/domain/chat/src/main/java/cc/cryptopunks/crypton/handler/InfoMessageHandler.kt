package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.ChatScope
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.Resource
import cc.cryptopunks.crypton.context.calculateId
import cc.cryptopunks.crypton.handle

internal fun ChatScope.handleSaveInfoMessage() =
    handle<Chat.Service.InfoMessage> {
        messageRepo.insertOrUpdate(
            Message(
                text = text,
                chat = chat.address,
                status = Message.Status.Info,
                to = Resource(address),
                from = Resource.Empty,
                timestamp = System.currentTimeMillis()
            ).calculateId()
        )
    }
