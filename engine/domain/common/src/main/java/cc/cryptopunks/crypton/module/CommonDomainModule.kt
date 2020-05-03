package cc.cryptopunks.crypton.module

import cc.cryptopunks.crypton.context.AppCore
import cc.cryptopunks.crypton.context.Route
import cc.cryptopunks.crypton.service.RouterService

class CommonDomainModule(
    appCore: AppCore
) {

    val routerService by lazy {
        RouterService(
            Route.Navigate(
                appCore.routeSys
            )
        )
    }
}
