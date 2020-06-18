package cc.cryptopunks.crypton.module

import cc.cryptopunks.crypton.context.AppScope
import cc.cryptopunks.crypton.context.Route
import cc.cryptopunks.crypton.interactor.IndicatorInteractor
import cc.cryptopunks.crypton.selector.HasAccountsSelector
import cc.cryptopunks.crypton.selector.NetworkStatusFlowSelector
import cc.cryptopunks.crypton.selector.NewSessionsFlowSelector
import cc.cryptopunks.crypton.service.AppService
import cc.cryptopunks.crypton.service.MainNavigationService

class AppDomainModule(
    private val appScope: AppScope
) : AppScope by appScope {

    val appService by lazy {
        AppService(
            appScope = appScope,
            hasAccounts = HasAccountsSelector(accountRepo),
            networkStatusFlow = NetworkStatusFlowSelector(networkSys),
            toggleIndicator = IndicatorInteractor(indicatorSys),
            newSessionsFlow = NewSessionsFlowSelector(sessionStore)
        )
    }

    val mainNavigationService by lazy {
        MainNavigationService(
            hasAccounts = HasAccountsSelector(accountRepo),
            navigate = Route.Navigate(routeSys)
        )
    }
}
