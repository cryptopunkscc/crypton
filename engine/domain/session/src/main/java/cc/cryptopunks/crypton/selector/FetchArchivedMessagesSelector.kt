package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

internal class FetchArchivedMessagesSelector(
    private val messageRepo: Message.Repo,
    private val messageNet: Message.Net
) {
    private val log = typedLog()

    suspend operator fun invoke(): Flow<List<Message>> {
        val since = messageRepo.latest()?.timestamp
        var count = 0
        return messageNet.readArchived(Message.Net.ReadArchived.Query(since = since))
            .onStart { log.d("Fetching archived messages since: $since") }
            .onEach { count += it.size }
            .onCompletion { log.d("Fetched $count messages") }
    }
}
