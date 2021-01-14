package cc.cryptopunks.crypton.interactor

import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.context.messageRepo
import cc.cryptopunks.crypton.logv2.log
import cc.cryptopunks.crypton.logv2.d

internal suspend fun SessionScope.flushQueuedMessages(
    filter: (Message) -> Boolean
) {
    messageRepo.listQueued().let { list: List<Message> ->
        log.d { "Flush pending messages $list" }
        list.filter(filter).forEach { message ->
            sendOrSubscribe(message)
        }
    }
}
