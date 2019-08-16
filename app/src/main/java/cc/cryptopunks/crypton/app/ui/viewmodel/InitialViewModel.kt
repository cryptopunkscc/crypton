package cc.cryptopunks.crypton.app.ui.viewmodel

import cc.cryptopunks.crypton.core.query.HasAccounts
import cc.cryptopunks.crypton.core.util.Schedulers
import cc.cryptopunks.crypton.app.R
import cc.cryptopunks.crypton.app.util.Navigation
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class InitialViewModel @Inject constructor(
    hasAccounts: HasAccounts,
    navigation: Navigation.Bus,
    schedulers: Schedulers
) : () -> Disposable by {
    hasAccounts()
        .subscribeOn(schedulers.io)
        .observeOn(schedulers.main)
        .subscribe { condition ->
            navigation.navigate(
                when (condition) {
                    true -> R.id.navigate_to_dashboardActivity
                    false -> R.id.action_splashFragment_to_set_account_navigation
                }
            )
        }
}