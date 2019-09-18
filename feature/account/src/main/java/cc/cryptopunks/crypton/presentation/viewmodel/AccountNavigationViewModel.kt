package cc.cryptopunks.crypton.presentation.viewmodel

import cc.cryptopunks.crypton.account.R
import cc.cryptopunks.crypton.domain.selector.NewAccountConnected
import cc.cryptopunks.crypton.util.Navigate
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.drop
import javax.inject.Inject

class AccountNavigationViewModel @Inject constructor(
    private val newAccountConnected: NewAccountConnected,
    private val navigate: Navigate
) {
    suspend operator fun invoke() = newAccountConnected().drop(1).collect {
        navigate(R.id.navigateAccountList)
    }
}