package cc.cryptopunks.crypton.interactor

import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.SessionScope

internal suspend fun SessionScope.flushQueuedMessages(
    filter: (Message) -> Boolean
) {
    messageRepo.queuedList().let { list: List<Message> ->
        log.d("Flush pending messages $list")
        list.filter(filter).forEach { message ->
            sendOrSubscribe(message)
        }
    }
}
