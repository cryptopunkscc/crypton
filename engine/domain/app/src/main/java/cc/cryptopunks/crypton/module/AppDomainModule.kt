package cc.cryptopunks.crypton.module

import cc.cryptopunks.crypton.context.AppCore
import cc.cryptopunks.crypton.context.Route
import cc.cryptopunks.crypton.context.Service
import cc.cryptopunks.crypton.factory.SessionFactory
import cc.cryptopunks.crypton.interactor.IndicatorInteractor
import cc.cryptopunks.crypton.interactor.InterruptSessionsInteractor
import cc.cryptopunks.crypton.interactor.InvokeSessionServicesInteractor
import cc.cryptopunks.crypton.interactor.LoadSessionsInteractor
import cc.cryptopunks.crypton.interactor.ReconnectSessionsInteractor
import cc.cryptopunks.crypton.interactor.SessionInteractor
import cc.cryptopunks.crypton.selector.HasAccountsSelector
import cc.cryptopunks.crypton.selector.NetworkStatusFlowSelector
import cc.cryptopunks.crypton.selector.NewSessionsFlowSelector
import cc.cryptopunks.crypton.service.AppService
import cc.cryptopunks.crypton.service.MainNavigationService

class AppDomainModule(
    private val appCore: AppCore
) : AppCore by appCore {

    val appService by lazy {
        val scope = Service.Scope()
        AppService(
            scope = scope,
            sessionInteractor = SessionInteractor(
                reconnect = ReconnectSessionsInteractor(sessionStore),
                interrupt = InterruptSessionsInteractor(sessionStore)
            ),
            hasAccounts = HasAccountsSelector(accountRepo),
            networkStatusFlow = NetworkStatusFlowSelector(networkSys),
            toggleIndicator = IndicatorInteractor(indicatorSys),
            loadSessions = LoadSessionsInteractor(
                accountRepo = accountRepo,
                sessionStore = sessionStore,
                createSession = SessionFactory(
                    accountRepo = accountRepo,
                    createConnection = createConnection,
                    createSessionRepo = createSessionRepo
                )
            ),
            newSessionsFlow = NewSessionsFlowSelector(sessionStore),
            invokeSessionServices = InvokeSessionServicesInteractor(appCore)
        )
    }

    val mainNavigationService by lazy {
        MainNavigationService(
            hasAccounts = HasAccountsSelector(accountRepo),
            navigate = Route.Navigate(routeSys)
        )
    }
}
