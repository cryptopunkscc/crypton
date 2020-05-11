package cc.cryptopunks.crypton.backend

import cc.cryptopunks.crypton.context.Connectable
import cc.cryptopunks.crypton.context.Route

internal class RequestBinding(
    private val createService: (Route) -> Connectable?,
    private val createBinding: ConnectableBindingFactory
) {
    suspend operator fun invoke(route: Route) = createBinding(route).apply {
        if (services.isEmpty()) plus(createService(route))
    }
}
