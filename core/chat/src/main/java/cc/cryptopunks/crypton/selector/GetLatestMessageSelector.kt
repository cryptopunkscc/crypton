package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.entity.Message
import cc.cryptopunks.crypton.entity.Session
import kotlinx.coroutines.Deferred
import javax.inject.Inject

class GetLatestMessageSelector @Inject constructor(
    private val messageRepo: Message.Repo,
    private val netScope: Session.Scope
) : () -> Deferred<Message?> by {
    netScope.async { messageRepo.latest() }
}