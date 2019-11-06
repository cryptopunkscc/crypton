package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.entity.Message
import cc.cryptopunks.crypton.entity.Session
import kotlinx.coroutines.Deferred
import javax.inject.Inject

class LatestMessageSelector @Inject constructor(
    private val messageRepo: Message.Repo,
    private val sessionScope: Session.Scope
) : () -> Deferred<Message?> by {
    sessionScope.async { messageRepo.latest() }
}