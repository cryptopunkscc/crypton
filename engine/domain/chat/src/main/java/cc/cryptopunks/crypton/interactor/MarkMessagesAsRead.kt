package cc.cryptopunks.crypton.interactor

import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.Session
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal class MarkMessagesAsRead(
    private val scope: Session.Scope,
    private val repo: Message.Repo
) : (List<Message>) -> Job {

    private val log = typedLog()

    override fun invoke(messages: List<Message>): Job = scope.launch {
        log.d("Read ${messages.size} messages")
        val read = messages.map {
            it.copy(
                readAt = System.currentTimeMillis()
            )
        }
        repo.run {
            insertOrUpdate(read)
            notifyUnread()
        }
    }
}
