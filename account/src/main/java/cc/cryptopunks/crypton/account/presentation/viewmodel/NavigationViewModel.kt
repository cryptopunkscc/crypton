package cc.cryptopunks.crypton.account.presentation.viewmodel

import cc.cryptopunks.crypton.account.R
import cc.cryptopunks.crypton.account.domain.query.NewAccountConnected
import cc.cryptopunks.crypton.core.util.Navigation
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class NavigationViewModel @Inject constructor(
    newAccountConnected: NewAccountConnected,
    navigation: Navigation.Bus
) : () -> Disposable by {
    newAccountConnected().subscribe {
        navigation.navigate(R.id.navigateAccountList)
    }
}