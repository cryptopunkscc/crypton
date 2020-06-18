package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.context.AppScope
import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.context.collect
import cc.cryptopunks.crypton.interactor.IndicatorInteractor
import cc.cryptopunks.crypton.handler.handleNetworkStatus
import cc.cryptopunks.crypton.interactor.invokeSessionServices
import cc.cryptopunks.crypton.interactor.loadSessions
import cc.cryptopunks.crypton.selector.HasAccountsSelector
import cc.cryptopunks.crypton.selector.NetworkStatusFlowSelector
import cc.cryptopunks.crypton.selector.NewSessionsFlowSelector
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AppService internal constructor(
    private val appScope: AppScope,

    private val hasAccounts: HasAccountsSelector,
    private val toggleIndicator: IndicatorInteractor,

    private val networkStatusFlow: NetworkStatusFlowSelector,

    private val newSessionsFlow: NewSessionsFlowSelector
) : AppScope by appScope {

    override val log = typedLog()

    operator fun invoke() = launch {
        log.d("Start")
        loadSessions()
        launch { hasAccounts().collect { toggleIndicator(it) } }
        launch { networkStatusFlow().collect(handleNetworkStatus()) }
        launch { newSessionsFlow().collect(SessionScope::invokeSessionServices) }
    }
}
