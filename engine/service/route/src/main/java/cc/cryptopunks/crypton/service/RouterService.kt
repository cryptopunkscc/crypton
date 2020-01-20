package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.context.Route
import cc.cryptopunks.crypton.context.Service
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.launch
import javax.inject.Inject

class RouterService @Inject constructor(
    private val navigate: Route.Api.Navigate
) : Service.Connectable {

    override val coroutineContext = SupervisorJob() + Dispatchers.Main

    override fun Service.Connector.connect(): Job = launch {
        input.filterIsInstance<Route>().collect {
            navigate(it)
        }
    }

    interface Core {
        val routerService: RouterService
    }
}