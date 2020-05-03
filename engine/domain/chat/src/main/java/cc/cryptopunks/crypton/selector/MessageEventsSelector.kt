package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.context.Message

internal class MessageEventsSelector(
    private val messageNet: Message.Net
) {
    operator fun invoke() = messageNet.messageEvents
}
