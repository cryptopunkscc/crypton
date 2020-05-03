package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.context.Route
import cc.cryptopunks.crypton.selector.NewAccountConnectedSelector
import kotlinx.coroutines.flow.collect

class AccountNavigationService internal constructor(
    private val newAccountConnected: NewAccountConnectedSelector,
    private val navigate: Route.Navigate
) {
    suspend operator fun invoke() = newAccountConnected().collect {
        navigate(Route.AccountList)
    }
}
