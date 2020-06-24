package cc.cryptopunks.crypton.interactor

import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.SessionScope


private var lastId = 0

internal suspend fun SessionScope.sendOrSubscribe(message: Message) {
    val id = lastId++

    log.d("$id start sending")
    when {
        message.to.address != address && !iAmSubscribed(message.chatAddress) -> {
            log.d("$id subscribe to ${message.to.address}")
            subscribe(message.to.address)
        }

        else -> {
            val job = sendMessage(message)
            log.d("$id sending")
            messageRepo.insertOrUpdate(message.copy(status = Message.Status.Sending))
            job.join()
            log.d("$id send")
            messageRepo.insertOrUpdate(message.copy(status = Message.Status.Sent))
        }
    }
}
