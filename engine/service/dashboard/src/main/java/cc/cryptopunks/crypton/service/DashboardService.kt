package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.context.Route
import cc.cryptopunks.crypton.context.Service
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class DashboardService @Inject constructor(
    private val navigate: Route.Api.Navigate
) : Service {

    object CreateChat
    object ManageAccounts

    override val coroutineContext = SupervisorJob() + Dispatchers.IO

    override fun Service.Connector.connect(): Job = launch {
        input.collect { arg ->
            when (arg) {
                is CreateChat -> navigate(Route.CreateChat)
                is ManageAccounts -> navigate(Route.AccountManagement)
            }
        }
    }

    interface Core {
        val dashboardService: DashboardService
    }
}