package cc.cryptopunks.crypton.presentation.viewmodel

import cc.cryptopunks.crypton.R
import cc.cryptopunks.crypton.domain.selector.HasAccountsSelector
import cc.cryptopunks.crypton.util.Navigate
import cc.cryptopunks.crypton.util.Scopes
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainNavigationViewModel @Inject constructor(
    hasAccounts: HasAccountsSelector,
    navigate: Navigate,
    scope: Scopes.ViewModel
) : () -> Job by {
    scope.launch {
        hasAccounts().collect { condition ->
            navigate(
                when (condition) {
                    true -> R.id.navigateDashboard
                    false -> R.id.navigateSetAccount
                }
            )
        }
    }
}