package cc.cryptopunks.crypton.app.presentation.viewmodel

import cc.cryptopunks.crypton.domain.query.HasAccounts
import cc.cryptopunks.crypton.app.R
import cc.cryptopunks.crypton.util.Schedulers
import cc.cryptopunks.crypton.util.Navigate
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class NavigationViewModel @Inject constructor(
    hasAccounts: HasAccounts,
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