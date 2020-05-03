package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Message
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull

internal class LatestMessageFlowSelector(
    repo: Message.Repo
) : (Chat) -> Flow<Message> by { chat ->
    repo.flowLatest(chat).filterNotNull()
}
