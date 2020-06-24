package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.context.AppScope
import cc.cryptopunks.crypton.context.collect
import cc.cryptopunks.crypton.handler.handleHasAccounts
import cc.cryptopunks.crypton.handler.handleNetworkStatus
import cc.cryptopunks.crypton.handler.handleNewSession
import cc.cryptopunks.crypton.interactor.loadSessions
import cc.cryptopunks.crypton.selector.hasAccountsFlow
import cc.cryptopunks.crypton.selector.networkStatusFlow
import cc.cryptopunks.crypton.selector.newSessionsFlow
import kotlinx.coroutines.launch

fun AppScope.startAppService() = launch {
    log.d("Start app service")
    loadSessions()
    launch { hasAccountsFlow().collect(handleHasAccounts()) }
    launch { networkStatusFlow().collect(handleNetworkStatus()) }
    launch { newSessionsFlow().collect(handleNewSession()) }
}
