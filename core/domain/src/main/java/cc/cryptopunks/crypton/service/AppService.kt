package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.ConnectableBinding
import cc.cryptopunks.crypton.ConnectorOutput
import cc.cryptopunks.crypton.actor
import cc.cryptopunks.crypton.connectable
import cc.cryptopunks.crypton.context.AppScope
import cc.cryptopunks.crypton.createHandlers
import cc.cryptopunks.crypton.handler.handleHasAccounts
import cc.cryptopunks.crypton.handler.handleSessionAction
import cc.cryptopunks.crypton.handler.handleStartServices
import cc.cryptopunks.crypton.interactor.loadSessions
import cc.cryptopunks.crypton.selector.hasAccountsFlow
import cc.cryptopunks.crypton.selector.sessionCommandFlow
import cc.cryptopunks.crypton.selector.startSessionServicesFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

fun AppScope.startAppService(out: ConnectorOutput = {}) = launch {
    log.d("Start app service")
    loadSessions()
    ConnectableBinding() + connectable() + actor { (_, _, out) ->
        launch { appActionsFlow().collect(out) }
    }
}

val appHandlers get() = createHandlers {
    +handleHasAccounts()
    +handleSessionAction()
    +handleStartServices()
} + sessionHandlers

private fun AppScope.appActionsFlow() = flowOf(
    hasAccountsFlow(),
    sessionCommandFlow(),
    startSessionServicesFlow()
).flattenMerge().onEach {
    log.d("App action flow $it")
}
