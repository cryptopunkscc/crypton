package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.annotation.ApplicationScope
import cc.cryptopunks.crypton.context.Service
import cc.cryptopunks.crypton.util.BroadcastError
import cc.cryptopunks.crypton.util.bind
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.Job
import javax.inject.Inject

@ApplicationScope
class ErrorService @Inject constructor(
    private val scope: Service.Scope,
    private val broadcastError: BroadcastError
) : () -> Job {

    private val log = typedLog()

    override fun invoke(): Job = let {
        log.d("start")
        scope.bind(broadcastError).apply {
            invokeOnCompletion {
                log.d("stop")
            }
        }
    }
}