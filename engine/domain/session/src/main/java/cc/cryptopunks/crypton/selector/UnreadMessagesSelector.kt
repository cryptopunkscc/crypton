package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.context.Message

internal class UnreadMessagesSelector(
    private val messageRepo: Message.Repo
) {
    operator fun invoke() = messageRepo.unreadListFlow()
}
