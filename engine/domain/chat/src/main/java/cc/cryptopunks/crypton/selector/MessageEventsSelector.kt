package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.flow.onEach

internal class MessageEventsSelector(
    private val messageNet: Message.Net
) {
    private val log = typedLog()
    operator fun invoke() = messageNet.messageEvents().onEach { log.d(it) }
}
