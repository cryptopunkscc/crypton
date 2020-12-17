package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.context.messageNet
import cc.cryptopunks.crypton.context.messageRepo
import cc.cryptopunks.crypton.util.logger.log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

internal suspend fun SessionScope.archivedMessagesFlow(): Flow<List<Message>> {
    val since = messageRepo.latest()?.timestamp
    var count = 0
    return messageNet.readArchived(Message.Net.ReadQuery(since = since))
        .onStart { log.d { "Fetching archived messages since: $since" } }
        .onEach { count += it.size }
        .onCompletion { log.d { "Fetched $count messages" } }
}
