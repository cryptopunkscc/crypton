package cc.cryptopunks.crypton.app.presentation.viewmodel

import cc.cryptopunks.crypton.account.domain.query.HasAccounts
import cc.cryptopunks.crypton.app.R
import cc.cryptopunks.crypton.common.Schedulers
import cc.cryptopunks.crypton.core.util.Navigate
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