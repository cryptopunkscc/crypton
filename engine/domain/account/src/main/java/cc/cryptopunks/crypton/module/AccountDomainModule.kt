package cc.cryptopunks.crypton.module

import cc.cryptopunks.crypton.context.AppScope
import cc.cryptopunks.crypton.context.Route
import cc.cryptopunks.crypton.interactor.DeleteAccountInteractor
import cc.cryptopunks.crypton.selector.AccountListSelector
import cc.cryptopunks.crypton.selector.NewAccountConnectedSelector
import cc.cryptopunks.crypton.service.AccountListService
import cc.cryptopunks.crypton.service.AccountNavigationService

class AccountDomainModule(
    appScope: AppScope
) : AppScope by appScope {

    val accountNavigationService by lazy {
        AccountNavigationService(
            navigate = Route.Navigate(routeSys),
            newAccountConnected = NewAccountConnectedSelector(sessionStore)
        )
    }

    val accountListService by lazy {
        AccountListService(
            getAccounts = AccountListSelector(accountRepo),
            deleteAccount = DeleteAccountInteractor(sessionStore, accountRepo)
        )
    }
}
