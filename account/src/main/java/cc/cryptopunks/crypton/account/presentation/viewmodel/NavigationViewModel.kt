package cc.cryptopunks.crypton.account.presentation.viewmodel

import cc.cryptopunks.crypton.account.R
import cc.cryptopunks.crypton.account.domain.query.NewAccountConnected
import cc.cryptopunks.crypton.core.util.Navigate
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class NavigationViewModel @Inject constructor(
    newAccountConnected: NewAccountConnected,
    navigate: Navigate
) : () -> Disposable by {
    newAccountConnected().subscribe {
        navigate(R.id.navigateAccountList)
    }
}