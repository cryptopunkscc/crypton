package cc.cryptopunks.crypton.interactor

import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.SessionScope

internal suspend fun SessionScope.sendMessages(
    list: List<Message>
) {
    log.d("Flush pending messages $list")
    list.forEach { message ->
        sendMessage(message)
    }
}
