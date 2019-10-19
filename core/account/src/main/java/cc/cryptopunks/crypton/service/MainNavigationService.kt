package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.selector.HasAccountsSelector
import cc.cryptopunks.crypton.navigation.Navigate
import cc.cryptopunks.crypton.navigation.Route
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class MainNavigationService @Inject constructor(
    private val hasAccounts: HasAccountsSelector,
    private val navigate: Navigate
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