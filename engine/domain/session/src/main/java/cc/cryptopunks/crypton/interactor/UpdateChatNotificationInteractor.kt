package cc.cryptopunks.crypton.interactor

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Connectable
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.Notification
import cc.cryptopunks.crypton.service.top

internal class UpdateChatNotificationInteractor(
    private val sys: Notification.Sys,
    private val store: Connectable.Binding.Store
) {
    private var current = emptyList<Message>()

    operator fun invoke(messages: List<Message>) {
        current.minus(messages).asNotifications().forEach { sys.cancel(it) }
        current = store.consume(messages)
        current.asNotifications().forEach { sys.show(it) }
    }

    private fun List<Message>.asNotifications() = groupBy { message: Message ->
        message.chatAddress
    }.map { (address: Address, messages: List<Message>) ->
        Notification.Messages(
            chatAddress = address,
            messages = messages
        )
    }

    private fun Connectable.Binding.Store.consume(messages: List<Message>) = top()
        ?.services
        ?.filterIsInstance<Message.Consumer>()
        ?.let { consumers ->
            messages.filterNot { message ->
                consumers.any { consumer ->
                    consumer.canConsume(message)
                }
            }
        }
        ?: messages
}
