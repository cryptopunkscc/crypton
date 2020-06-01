package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Message

internal class MessageListSelector(
    private val repo: Message.Repo
) {
    suspend operator fun invoke(
        chat: Chat? = null
    ): List<Message> = repo.list(
        range = System.currentTimeMillis().let { currentTime ->
            currentTime - SEVEN_DAYS_MILLIS..currentTime
        }
    ).let { messages ->
        when {
            chat != null -> messages.filter { it.chatAddress == chat.address }
            else -> messages
        }
    }

    private companion object {
        const val SEVEN_DAYS_MILLIS = 1000 * 60 * 60 * 24 * 7
    }
}
