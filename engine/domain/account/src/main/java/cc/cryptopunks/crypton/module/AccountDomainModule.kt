package cc.cryptopunks.crypton.module

import cc.cryptopunks.crypton.context.AppCore
import cc.cryptopunks.crypton.context.Route
import cc.cryptopunks.crypton.interactor.AddAccountInteractor
import cc.cryptopunks.crypton.interactor.DeleteAccountInteractor
import cc.cryptopunks.crypton.selector.AccountListSelector
import cc.cryptopunks.crypton.selector.NewAccountConnectedSelector
import cc.cryptopunks.crypton.service.AccountListService
import cc.cryptopunks.crypton.service.AccountNavigationService
import cc.cryptopunks.crypton.service.CreateAccountService

class AccountDomainModule(
    appCore: AppCore
) : AppCore by appCore {

    val createAccountService by lazy {
        CreateAccountService(
            addAccount = AddAccountInteractor(
                accountRepo = accountRepo,
                createConnection = createConnection,
                sessionStore = sessionStore,
                createRepo = createSessionRepo
            )
        )
    }

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
