package cc.cryptopunks.crypton.presenter

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.util.OptionItemNavigationService
import cc.cryptopunks.crypton.selector.AccountListSelector
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class AccountListPresenter @Inject constructor(
    private val getAccounts: AccountListSelector,
    private val navigationService: OptionItemNavigationService
) : Presenter<AccountListPresenter.Actor>{

    override suspend fun Actor.invoke() = coroutineScope {
        navigationService()
        launch { getAccounts().collect(setAccounts) }
    }

    interface Actor {
        val setAccounts: suspend (List<Address>) -> Unit
    }
}