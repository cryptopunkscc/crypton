package cc.cryptopunks.crypton.backend

import cc.cryptopunks.crypton.context.AppCore
import cc.cryptopunks.crypton.context.Connectable
import cc.cryptopunks.crypton.context.Route
import cc.cryptopunks.crypton.module.AppDomainModule
import cc.cryptopunks.crypton.service.AppService
import cc.cryptopunks.crypton.util.Store
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull

class Backend(
    appCore: AppCore
) : AppCore by appCore {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Unconfined)

    private val stack: Store<List<Context>> = Store(emptyList())

    override val routeSys: Route.Sys = createRouteSys()

    override val navigate: Route.Navigate = Route.Navigate(routeSys)

    val request = RequestBinding(
        createService = ServiceFactory(this),
        createBinding = ConnectableBindingFactory(
            bindingStore = connectableBindingsStore,
            stack = stack
        )
    )::invoke

    val drop = DropBinding(
        scope = scope,
        stack = stack
    )::invoke

    val top = GetTopBinding(stack)::invoke

    val appService: AppService by lazy {
        AppDomainModule(this).appService
    }

    val bindingFlow: () -> Flow<Connectable.Binding> = {
        stack.changesFlow().mapNotNull { it.lastOrNull()?.binding }
    }
}
