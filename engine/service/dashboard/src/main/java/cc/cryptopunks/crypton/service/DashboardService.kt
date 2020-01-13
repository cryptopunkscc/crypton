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

    override val coroutineContext = SupervisorJob() + Dispatchers.IO

    override fun Service.Binding.bind(): Job = launch {
        input.collect { arg ->
            when (arg) {
                is Input.CreateChat -> navigate(Route.CreateChat)
                is Input.ManageAccounts -> navigate(Route.AccountManagement)
            }
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