package cc.cryptopunks.crypton.interactor

import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.context.createChat

internal suspend fun SessionScope.saveMessages(messages: List<Message>) {
    messages.forEach { saveMessage(it) }
}

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
    chatAddress = createChat(
        Chat.Service.CreateChatData(
            title = message.chatAddress.id,
            users = listOf(message.getParty(address).address)
        )
    ).address
)
