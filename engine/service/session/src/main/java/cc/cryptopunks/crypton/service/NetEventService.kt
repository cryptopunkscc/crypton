package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.annotation.SessionScope
import cc.cryptopunks.crypton.api.Api
import cc.cryptopunks.crypton.context.Session
import cc.cryptopunks.crypton.net.Net
import cc.cryptopunks.crypton.util.BroadcastError
import cc.cryptopunks.crypton.util.ext.invokeOnClose
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@SessionScope
class NetEventService @Inject constructor(
    private val sessionScope: Session.Scope,
    private val netEvents: Net.Event.Output,
    private val broadcastError: BroadcastError
) : () -> Job {

    private val log = typedLog()

    override fun invoke(): Job = sessionScope.launch {
        log.d("start")
        invokeOnClose { log.d("stop") }
        netEvents.collect { event ->
            handleEvent(event)
        }
    }

    private suspend fun handleEvent(event: Api.Event) {
        log.d(event)

        if (event is Net.Event.Disconnected)
            handleDisconnected(event)
    }


    private suspend fun handleDisconnected(event: Net.Event.Disconnected) {
        event.throwable?.let { throwable ->
            log.e(throwable)
            broadcastError.send(throwable)
        }
    }
}