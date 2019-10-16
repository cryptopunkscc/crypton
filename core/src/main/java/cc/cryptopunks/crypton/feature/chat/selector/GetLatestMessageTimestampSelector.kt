package cc.cryptopunks.crypton.feature.chat.selector

import cc.cryptopunks.crypton.api.Api
import cc.cryptopunks.crypton.entity.Message
import kotlinx.coroutines.Deferred
import javax.inject.Inject

class GetLatestMessageTimestampSelector @Inject constructor(
    private val messageRepo: Message.Repo,
    private val apiScope: Api.Scope
) : () -> Deferred<Long> by {
    apiScope.async { messageRepo.latest()?.timestamp ?: 0 }
}