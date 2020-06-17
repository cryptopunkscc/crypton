package cc.cryptopunks.crypton.mock

import cc.cryptopunks.crypton.context.Route
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MockRouteSys :
    Route.Sys,
    CoroutineScope {

    override val coroutineContext: CoroutineContext = SupervisorJob() + Dispatchers.Unconfined

    private val log = typedLog()

    override fun navigate(route: Route) {
        log.d("navigate $route")
    }

    override suspend fun bind(navigator: Any) {
        log.d("Bind $navigator")
    }
}
