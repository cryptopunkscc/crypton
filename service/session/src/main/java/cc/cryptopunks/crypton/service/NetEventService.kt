package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.entity.Session
import cc.cryptopunks.crypton.net.Net
import cc.cryptopunks.crypton.util.BroadcastError
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class NetEventService @Inject constructor(
    private val sessionScope: Session.Scope,
    private val netEvents: Net.Event.Output,
    private val broadcastError: BroadcastError
) : () -> Job {

    private val log = typedLog()

    override fun invoke(): Job = sessionScope.launch {
        netEvents.collect { event ->
            handleEvent(event)
        }
    }

    private suspend fun handleEvent(event: Net.Event) {
        log.d(event)

        if (event is Net.Event.ConnectionClosed)
            handleError(event)
    }


    private suspend fun handleError(event: Net.Event.ConnectionClosed) {
        event.throwable?.let { throwable ->
            log.e(throwable)
            broadcastError.send(throwable)
        }
    }
}