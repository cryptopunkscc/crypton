package cc.cryptopunks.crypton.backend.internal

import cc.cryptopunks.crypton.Connectable
import cc.cryptopunks.crypton.context.Route

internal fun requestBindingInteractor(
    createService: (Route<*>) -> Connectable?,
    createBinding: ConnectableBindingFactory
): (Route<*>) -> Connectable.Binding = { route ->
    createBinding(route).apply {
        if (services.isEmpty()) plus(createService(route))
    }
}
