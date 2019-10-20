package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.net.Net
import cc.cryptopunks.crypton.entity.Message
import kotlinx.coroutines.Deferred
import javax.inject.Inject

class GetLatestMessageTimestampSelector @Inject constructor(
    private val messageRepo: Message.Repo,
    private val netScope: Net.Scope
) : () -> Deferred<Long> by {
    netScope.async { messageRepo.latest()?.timestamp ?: 0 }
}