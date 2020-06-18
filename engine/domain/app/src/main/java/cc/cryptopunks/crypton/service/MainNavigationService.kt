package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.context.AppScope
import cc.cryptopunks.crypton.context.Route
import cc.cryptopunks.crypton.selector.hasAccountsFlow
import kotlinx.coroutines.flow.collect

suspend fun AppScope.startMainNavigationService() =
    hasAccountsFlow().collect { condition ->
        navigate(
            when (condition) {
                true -> Route.Dashboard
                false -> Route.SetAccount
            }
        )
    }
