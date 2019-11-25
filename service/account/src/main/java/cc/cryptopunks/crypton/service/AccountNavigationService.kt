package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.navigation.Navigate
import cc.cryptopunks.crypton.navigation.Route
import cc.cryptopunks.crypton.selector.NewAccountConnectedSelector
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class AccountNavigationService @Inject constructor(
    private val newAccountConnected: NewAccountConnectedSelector,
    private val navigate: Navigate
) {
    suspend operator fun invoke() = newAccountConnected().collect {
        navigate(Route.AccountList)
    }

    interface Component {
        val accountNavigationService: AccountNavigationService
    }
}