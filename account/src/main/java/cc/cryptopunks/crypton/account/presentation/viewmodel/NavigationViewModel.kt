package cc.cryptopunks.crypton.account.presentation.viewmodel

import cc.cryptopunks.crypton.account.R
import cc.cryptopunks.crypton.account.domain.query.NewConnectedAccounts
import cc.cryptopunks.crypton.common.Schedulers
import cc.cryptopunks.crypton.core.util.Navigation
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class NavigationViewModel @Inject constructor(
    newConnectedAccounts: NewConnectedAccounts,
    navigation: Navigation.Bus,
    schedulers: Schedulers
) : () -> Disposable by {
    newConnectedAccounts()
        .skip(1)
        .subscribeOn(schedulers.io)
        .observeOn(schedulers.main)
        .subscribe { navigation.navigate(R.id.navigate_to_accountListFragment) }
}