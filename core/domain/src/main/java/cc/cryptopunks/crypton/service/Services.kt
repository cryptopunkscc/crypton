package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.Output
import cc.cryptopunks.crypton.actor
import cc.cryptopunks.crypton.service
import cc.cryptopunks.crypton.context.RootScope
import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.interactor.loadSessions
import cc.cryptopunks.crypton.plus
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

fun RootScope.startAppService(out: Output = {}) = launch {
    log.d("Start app service")
    loadSessions()
    service() + actor { (_, _, out) -> appActionsFlow().collect(out) }
}

internal fun SessionScope.startSessionService(out: Output = {}) {
    log.d("Invoke session services for $address")
    service() + actor { (_, _, out) -> accountActionsFlow().collect(out) }
}
