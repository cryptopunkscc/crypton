package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Message
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull

internal class LatestMessageFlowSelector(
    repo: Message.Repo
) : (Address) -> Flow<Message> by { chatAddress ->
    repo.flowLatest(chatAddress).filterNotNull()
}
