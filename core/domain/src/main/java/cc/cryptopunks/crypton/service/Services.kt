package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.Output
import cc.cryptopunks.crypton.actor
import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.context.RootScope
import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.interactor.loadSessions
import cc.cryptopunks.crypton.plus
import cc.cryptopunks.crypton.service
import cc.cryptopunks.crypton.serviceName
import cc.cryptopunks.crypton.util.logger.CoroutineLog
import cc.cryptopunks.crypton.util.logger.log
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

fun RootScope.startAppService(out: Output = {}) = launch {
    log.d { "Start app service" }
    loadSessions()
    service("Root".serviceName) + actor(
        coroutineContext
            .minusKey(Job)
            .plus(CoroutineLog.Label("RootEmitter"))
    ) { (_, _, out) ->
        appActionsFlow().collect(out)
    }
}

internal suspend fun SessionScope.startSessionService(out: Output = {}) {
    log.d { "Invoke session services for $address" }
    service("Session".serviceName) + actor(
        coroutineContext
            .minusKey(Job)
            .plus(CoroutineLog.Label("SessionEmitter"))
            .plus(CoroutineLog.Action(Exec.SessionService))
    ) { (_, _, out) ->
        accountActionsFlow().collect(out)
    }
}
