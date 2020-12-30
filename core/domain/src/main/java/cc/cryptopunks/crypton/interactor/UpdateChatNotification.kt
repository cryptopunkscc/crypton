package cc.cryptopunks.crypton.interactor

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.Notification
import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.context.account
import cc.cryptopunks.crypton.context.messageConsumers
import cc.cryptopunks.crypton.context.navigateChatId
import cc.cryptopunks.crypton.context.notificationSys
import cc.cryptopunks.crypton.context.top


fun updateChatNotification(): SessionScope.(List<Message>) -> Unit {
    var current = emptyList<Message>()
    return { messages ->
        val navigateChatId = navigateChatId.value
        val notificationSys = notificationSys

        current.minus(messages).asNotifications(account.address, navigateChatId).forEach {
            notificationSys.cancel(it)
        }

        current = messageConsumers.consume(messages)

        current.asNotifications(account.address, navigateChatId).forEach {
            notificationSys.show(it)
        }
    }
}

private fun List<Message>.asNotifications(
    account: Address,
    destination: Int,
) = groupBy { message: Message ->
    message.chat
}.map { (address: Address, messages: List<Message>) ->
    Notification.Messages(
        chatAddress = address,
        messages = messages,
        account = account,
        destination = destination
    )
}

private fun Message.Consumer.Store.consume(messages: List<Message>) = top()
    ?.let { consumer -> messages.filterNot(consumer::canConsume) }
    ?: messages
