package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.handle
import cc.cryptopunks.crypton.interactor.updateChatNotification
import cc.cryptopunks.crypton.util.logger.log

internal fun handleUpdateChatNotification(
    updateChatNotification: SessionScope.(List<Message>) -> Unit = updateChatNotification()
) =
    handle { _, (messages): Exec.UpdateNotification ->
        log.d { "update chat notification $messages" }
        updateChatNotification(messages)
    }
