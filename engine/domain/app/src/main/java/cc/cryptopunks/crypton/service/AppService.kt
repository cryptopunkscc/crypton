package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.context.Service
import cc.cryptopunks.crypton.interactor.IndicatorInteractor
import cc.cryptopunks.crypton.interactor.InvokeSessionServicesInteractor
import cc.cryptopunks.crypton.interactor.LoadSessionsInteractor
import cc.cryptopunks.crypton.interactor.SessionInteractor
import cc.cryptopunks.crypton.selector.HasAccountsSelector
import cc.cryptopunks.crypton.selector.NetworkStatusFlowSelector
import cc.cryptopunks.crypton.selector.NewSessionsFlowSelector
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AppService internal constructor(
    private val scope: Service.Scope,

    private val loadSessions: LoadSessionsInteractor,

    private val hasAccounts: HasAccountsSelector,
    private val toggleIndicator: IndicatorInteractor,

    private val networkStatusFlow: NetworkStatusFlowSelector,
    private val sessionInteractor: SessionInteractor,

    private val newSessionsFlow: NewSessionsFlowSelector,
    private val invokeSessionServices: InvokeSessionServicesInteractor
) {
    private val log = typedLog()
    operator fun invoke() = scope.launch {
        log.d("Start")
        loadSessions()
        launch { hasAccounts().collect { toggleIndicator(it) } }
        launch { networkStatusFlow().collect { sessionInteractor(it) } }
        launch { newSessionsFlow().collect { invokeSessionServices(it) } }
    }
}
