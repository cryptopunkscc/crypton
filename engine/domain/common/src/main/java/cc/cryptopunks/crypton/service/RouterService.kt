package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.context.AppScope
import cc.cryptopunks.crypton.context.Connectable
import cc.cryptopunks.crypton.context.Connector
import cc.cryptopunks.crypton.context.Route
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.launch

class RouterService(
    appScope: AppScope
) : Connectable,
    AppScope by appScope {

    override fun Connector.connect(): Job = launch {
        input.filterIsInstance<Route>().collect {
            navigate(it)
        }
    }
}
