package cc.cryptopunks.crypton.sys

import cc.cryptopunks.crypton.context.Route
import cc.cryptopunks.crypton.util.typedLog

class RouteSys : Route.Sys {
    private val log = typedLog()
    override fun navigate(route: Route) {
        log.d("Navigate $route")
    }

    override suspend fun bind(navigator: Any) {
        log.d("Bind $navigator")
    }
}
