package cc.cryptopunks.crypton.interactor

import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.context.insertChat

internal suspend fun SessionScope.saveMessage(message: Message) {
    message.run {
        get(message) ?: create(message)
    }.let { prepared ->
        messageRepo.run {
            log.d("inserting message $message")
            insertOrUpdate(prepared)
            if (prepared.status == Message.Status.Received)
                notifyUnread()
        }
    }
}

private suspend fun SessionScope.get(message: Message) = messageRepo.run {
    get(message.id)?.also {
        delete(it)
    }?.copy(
        status = message.status
    )
}

private suspend fun SessionScope.create(message: Message) = message.copy(
    chat = Chat(
        account = address,
        address = message.chat
    ).also {
        insertChat(it)
    }.address
)
