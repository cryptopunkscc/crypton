package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.SessionScope
import kotlinx.coroutines.flow.filterNotNull

internal fun SessionScope.latestMessageFlow(chatAddress: Address) =
    messageRepo.flowLatest(chatAddress).filterNotNull()
