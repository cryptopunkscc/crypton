package cc.cryptopunks.crypton.module

import cc.cryptopunks.crypton.context.AppScope
import cc.cryptopunks.crypton.context.Route
import cc.cryptopunks.crypton.service.RouterService

class CommonDomainModule(
    appScope: AppScope
) {

    val routerService by lazy {
        RouterService(
            Route.Navigate(
                appScope.routeSys
            )
        )
    }
}
