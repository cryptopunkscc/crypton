package cc.cryptopunks.crypton.presentation.viewmodel

import cc.cryptopunks.crypton.domain.selector.HasAccountsSelector
import cc.cryptopunks.crypton.R
import cc.cryptopunks.crypton.util.Schedulers
import cc.cryptopunks.crypton.util.Navigate
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class MainNavigationViewModel @Inject constructor(
    hasAccounts: HasAccountsSelector,
    navigate: Navigate,
    schedulers: Schedulers
) : () -> Disposable by {
    hasAccounts()
        .subscribeOn(schedulers.io)
        .observeOn(schedulers.main)
        .subscribe { condition ->
            navigate(
                when (condition) {
                    true -> R.id.navigateDashboard
                    false -> R.id.navigateSetAccount
                }
            )
        }
}