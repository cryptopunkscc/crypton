package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.context.Actor
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.util.Store

internal class CanConsumeSelector(
    private val chat: Chat,
    private val store: Store<Actor.Status>
) : Message.Consumer {
    override fun canConsume(message: Message): Boolean =
        message.chatAddress == chat.address && store.get() == Actor.Start
}
