package cc.cryptopunks.crypton.backend.internal

import cc.cryptopunks.crypton.Connectable
import cc.cryptopunks.crypton.context.Route
import cc.cryptopunks.crypton.createBinding
import cc.cryptopunks.crypton.util.Store

internal class ConnectableBindingFactory(
    private val bindingStore: Connectable.Binding.Store,
    private val stack: Store<List<Context>>
) {
    operator fun invoke(route: Route<*>): Connectable.Binding =
        get(route) ?: create(route)

    private fun get(route: Route<*>) = stack.get().lastOrNull()?.takeIf {
        it.route == route
    }?.binding

    private fun create(route: Route<*>) = Context(
        route = route,
        binding = bindingStore.createBinding()
    ).also { context ->
        stack { plus(context) }
    }.binding
}
