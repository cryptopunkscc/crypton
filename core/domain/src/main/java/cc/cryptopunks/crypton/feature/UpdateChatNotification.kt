package cc.cryptopunks.crypton.feature

import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.context.SessionScopeTag
import cc.cryptopunks.crypton.context.messageRepo
import cc.cryptopunks.crypton.emitter
import cc.cryptopunks.crypton.feature
import cc.cryptopunks.crypton.interactor.updateChatNotification
import cc.cryptopunks.crypton.util.ext.bufferedThrottle
import cc.cryptopunks.crypton.util.logger.log
import kotlinx.coroutines.flow.map

internal fun updateChatNotification(
    updateNotification: SessionScope.(List<Message>) -> Unit = updateChatNotification()
) = feature(

    emitter = emitter(SessionScopeTag) {
        messageRepo.flowListUnread().bufferedThrottle(500).map {
            Exec.UpdateNotification(it.flatten())
        }
    },

    handler = { _, (messages): Exec.UpdateNotification ->
        log.d { "update chat notification $messages" }
        updateNotification(messages)
    }
)
