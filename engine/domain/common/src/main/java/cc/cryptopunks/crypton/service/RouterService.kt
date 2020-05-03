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

class RouterService(
    private val navigate: Route.Navigate
) : Connectable {

    override val coroutineContext = SupervisorJob() + Dispatchers.Main

    override fun Connector.connect(): Job = launch {
        input.filterIsInstance<Route>().collect {
            navigate(it)
        }
    }
}
