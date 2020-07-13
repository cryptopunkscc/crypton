package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.SessionScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

internal suspend fun SessionScope.fetchArchivedMessages(): Flow<List<Message>> {
    val since = messageRepo.latest()?.timestamp
    var count = 0
    return readArchived(Message.Net.ReadArchived.Query(since = since))
        .onStart { log.d("Fetching archived messages since: $since") }
        .onEach { count += it.size }
        .onCompletion { log.d("Fetched $count messages") }
}
