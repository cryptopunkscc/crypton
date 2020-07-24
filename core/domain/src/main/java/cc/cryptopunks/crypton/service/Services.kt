package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.ConnectorOutput
import cc.cryptopunks.crypton.actor
import cc.cryptopunks.crypton.connectable
import cc.cryptopunks.crypton.context.AppScope
import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.interactor.loadSessions
import cc.cryptopunks.crypton.plus
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

fun AppScope.startAppService(out: ConnectorOutput = {}) = launch {
    log.d("Start app service")
    loadSessions()
    connectable() + actor { (_, _, out) -> appActionsFlow().collect(out) }
}

internal fun SessionScope.startSessionService(out: ConnectorOutput = {}) {
    log.d("Invoke session services for $address")
    connectable() + actor { (_, _, out) -> accountActionsFlow().collect(out) }
}
