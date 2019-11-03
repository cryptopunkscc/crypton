package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.selector.NewAccountConnectedSelector
import cc.cryptopunks.crypton.navigation.Navigate
import cc.cryptopunks.crypton.navigation.Route
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.drop
import javax.inject.Inject

class AccountNavigationService @Inject constructor(
    private val newAccountConnected: NewAccountConnectedSelector,
    private val navigate: Navigate
) {
    suspend operator fun invoke() = newAccountConnected().drop(1).collect {
        navigate(Route.AccountList)
    }
}