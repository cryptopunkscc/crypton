package cc.cryptopunks.crypton.presentation.viewmodel

import cc.cryptopunks.crypton.account.R
import cc.cryptopunks.crypton.domain.query.NewAccountConnected
import cc.cryptopunks.crypton.util.Navigate
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class AccountNavigationViewModel @Inject constructor(
    newAccountConnected: NewAccountConnected,
    navigate: Navigate
) : () -> Disposable by {
    newAccountConnected().subscribe {
        navigate(R.id.navigateAccountList)
    }
}