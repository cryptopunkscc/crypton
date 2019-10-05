package cc.cryptopunks.crypton.presentation.viewmodel

import cc.cryptopunks.crypton.feature.Route
import cc.cryptopunks.crypton.feature.account.selector.HasAccountsSelector
import cc.cryptopunks.crypton.util.Navigate
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class MainNavigationViewModel @Inject constructor(
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