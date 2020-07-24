package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.handle
import cc.cryptopunks.crypton.interactor.updateChatNotification

internal fun handleUpdateChatNotification(
    updateChatNotification: SessionScope.(List<Message>) -> Unit = updateChatNotification()
) =
    handle { _, (messages): Chat.Service.UpdateNotification ->
        log.d("update chat notification $messages")
        updateChatNotification(messages)
    }
