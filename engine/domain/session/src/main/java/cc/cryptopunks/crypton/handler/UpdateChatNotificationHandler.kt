package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.AppScope
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.handle
import cc.cryptopunks.crypton.interactor.updateChatNotification

internal fun AppScope.handleUpdateChatNotification(
    updateChatNotification: (List<Message>) -> Unit = updateChatNotification()
) =
    handle<Chat.Service.UpdateNotification> {
        log.d("update chat notification $messages")
        updateChatNotification(messages)
    }
