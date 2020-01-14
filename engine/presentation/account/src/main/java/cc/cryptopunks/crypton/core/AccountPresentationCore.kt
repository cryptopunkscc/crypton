package cc.cryptopunks.crypton.core

import cc.cryptopunks.crypton.presenter.AccountListPresenter
import cc.cryptopunks.crypton.util.OptionItemNavigationService
import cc.cryptopunks.crypton.viewmodel.AccountItemViewModel
import javax.inject.Provider

interface AccountPresentationCore {
    val accountListPresenter: AccountListPresenter
    val accountItemViewModelProvider: Provider<AccountItemViewModel>
    val navigationService: OptionItemNavigationService
}