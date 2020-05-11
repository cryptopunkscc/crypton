package cc.cryptopunks.crypton.sys

import cc.cryptopunks.crypton.backend.Backend
import cc.cryptopunks.crypton.context.Route
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class RouteSys :
    Route.Sys,
    CoroutineScope {

    override val coroutineContext: CoroutineContext = SupervisorJob() + Dispatchers.Unconfined
    private val log = typedLog()

    private var backend: Backend? = null

    override fun navigate(route: Route) {
        log.d("Navigate $route")
        backend?.run {
            when (route) {
                is Route.Back -> drop()
                else -> launch { request(route) }
            }
        }
    }

    override suspend fun bind(navigator: Any) {
        log.d("Bind $navigator")
        backend = navigator as Backend
    }
}
