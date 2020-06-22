package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.AppScope
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Handle
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.handle
import cc.cryptopunks.crypton.interactor.updateChatNotification
import kotlinx.coroutines.launch

internal fun AppScope.handleUpdateChatNotification(
    updateChatNotification: (List<Message>) -> Unit = updateChatNotification()
): Handle<Chat.Service.UpdateNotification> {
    return handle {
        launch {
            log.d("update chat notification $messages")
            updateChatNotification(messages)
        }
    }
}
