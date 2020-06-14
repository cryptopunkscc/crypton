package cc.cryptopunks.crypton.interactor

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.Session
import kotlinx.coroutines.flow.collect

internal suspend fun Session.flushQueuedMessages() =
    flushQueuedMessages { true }

internal suspend fun Session.flushQueuedMessages(
    address: Address
) = flushQueuedMessages { message: Message ->
    message.chatAddress == address
}

internal suspend fun Session.flushQueuedMessages(
    filter: (Message) -> Boolean
) {
    log.d("Start")

    messageRepo.queuedListFlow().collect { list: List<Message> ->
        log.d("Flush queue $list")
        list.filter(filter).forEach { message ->
            sendMessage(message)
        }
    }
}
