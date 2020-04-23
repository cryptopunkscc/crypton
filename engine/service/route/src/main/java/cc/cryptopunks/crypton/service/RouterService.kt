package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.context.Connectable
import cc.cryptopunks.crypton.context.Connector
import cc.cryptopunks.crypton.context.Route
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.launch
import javax.inject.Inject

class RouterService @Inject constructor(
    private val navigate: Route.Api.Navigate
) : Connectable {

    override val coroutineContext = SupervisorJob() + Dispatchers.Main

    override fun Connector.connect(): Job = launch {
        input.filterIsInstance<Route>().collect {
            navigate(it)
        }
    }

    interface Core {
        val routerService: RouterService
    }
}