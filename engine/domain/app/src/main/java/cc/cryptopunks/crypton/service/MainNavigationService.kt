package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.context.Route
import cc.cryptopunks.crypton.selector.HasAccountsSelector
import kotlinx.coroutines.flow.collect

class MainNavigationService internal constructor(
    private val hasAccounts: HasAccountsSelector,
    private val navigate: Route.Navigate
) {
    suspend operator fun invoke() = hasAccounts().collect { condition ->
        navigate(
            when (condition) {
                true -> Route.Dashboard
                false -> Route.SetAccount
            }
        )
    }
}
