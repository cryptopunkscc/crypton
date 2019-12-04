package cc.cryptopunks.crypton.presenter

import cc.cryptopunks.crypton.context.Feature
import cc.cryptopunks.crypton.context.Route
import cc.cryptopunks.crypton.context.Service
import javax.inject.Inject

class DashboardService @Inject constructor(
    override val scope: Feature.Scope,
    private val navigate: Route.Api.Navigate
) : Service.Abstract() {

    override suspend fun Any.onInput() {
        when(this) {
            is Input.CreateChat -> navigate(Route.CreateChat)
            is Input.ManageAccounts -> navigate(Route.AccountManagement)
        }
    }

    interface Input {
        object CreateChat : Input
        object ManageAccounts : Input
    }

    interface Core {
        val dashboardService: DashboardService
    }
}