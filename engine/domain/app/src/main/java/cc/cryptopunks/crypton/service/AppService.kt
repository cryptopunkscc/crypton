package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.context.Network
import cc.cryptopunks.crypton.context.Service
import cc.cryptopunks.crypton.interactor.SessionInteractor
import cc.cryptopunks.crypton.interactor.IndicatorInteractor
import cc.cryptopunks.crypton.interactor.InvokeSessionServicesInteractor
import cc.cryptopunks.crypton.interactor.LoadSessionsInteractor
import cc.cryptopunks.crypton.selector.HasAccountsSelector
import cc.cryptopunks.crypton.selector.NewSessionsFlowSelector
import cc.cryptopunks.crypton.util.ext.bufferedThrottle
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AppService internal constructor(
    private val scope: Service.Scope,

    private val loadSessions: LoadSessionsInteractor,

    private val hasAccounts: HasAccountsSelector,
    private val toggleIndicator: IndicatorInteractor,

    private val networkSys: Network.Sys,
    private val sessionInteractor: SessionInteractor,

    private val newSessionsFlow: NewSessionsFlowSelector,
    private val invokeSessionServices: InvokeSessionServicesInteractor
) {

    operator fun invoke() = scope.launch {
        loadSessions()
        launch { hasAccounts().collect { toggleIndicator(it) } }
        launch { networkSys.statusFlow().bufferedThrottle(200).collect { sessionInteractor(it.last()) } }
        launch { newSessionsFlow().collect { invokeSessionServices(it) } }
    }
}
