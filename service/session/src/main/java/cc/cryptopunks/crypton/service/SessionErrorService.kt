package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.entity.Session
import cc.cryptopunks.crypton.util.BroadcastError
import cc.cryptopunks.crypton.util.bind
import kotlinx.coroutines.Job
import javax.inject.Inject

class SessionErrorService @Inject constructor(
    private val scope: Session.Scope,
    private val broadcastError: BroadcastError
) : () -> Job by {
    scope.bind(broadcastError)
}