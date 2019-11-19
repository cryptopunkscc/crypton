package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.util.BroadcastError
import cc.cryptopunks.crypton.util.bind
import kotlinx.coroutines.Job
import javax.inject.Inject

class ErrorService @Inject constructor(
    private val scope: Service.Scope,
    private val broadcastError: BroadcastError
) : () -> Job by {
    scope.bind(broadcastError)
}