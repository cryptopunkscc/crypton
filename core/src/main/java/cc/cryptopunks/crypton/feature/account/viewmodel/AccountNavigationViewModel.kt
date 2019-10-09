package cc.cryptopunks.crypton.feature.account.viewmodel

import cc.cryptopunks.crypton.feature.Route
import cc.cryptopunks.crypton.feature.account.selector.NewAccountConnectedSelector
import cc.cryptopunks.crypton.util.Navigate
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.drop
import javax.inject.Inject

class AccountNavigationViewModel @Inject constructor(
    private val newAccountConnected: NewAccountConnectedSelector,
    private val navigate: Navigate
) {
    suspend operator fun invoke() = newAccountConnected().drop(1).collect {
        navigate(Route.AccountList)
    }
}