package cc.cryptopunks.crypton.interactor

import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.context.isConference
import cc.cryptopunks.crypton.util.logger.log

private var lastId = 0

internal suspend fun SessionScope.sendOrSubscribe(message: Message) {
    val rosterNet = rosterNet
    val id = lastId++

    log.d { "$id start sending" }
    when {
        message.to.address == account.address -> return

        !message.chat.isConference && !rosterNet.iAmSubscribed(message.chat) ->
            if (message.chat !in subscriptions.get()) {
                log.d { "$id subscribe to ${message.to.address}" }
                subscriptions reduce { plus(message.chat) }
                rosterNet.subscribe(message.to.address)
            }

        else -> {
            val messageRepo = messageRepo
            val job = messageNet.sendMessage(message)

            log.d { "$id sending" }
            messageRepo.insertOrUpdate(message.copy(status = Message.Status.Sending))
            job.join()
            log.d { "$id send" }
            messageRepo.insertOrUpdate(message.copy(status = Message.Status.Sent))
        }
    }
}
