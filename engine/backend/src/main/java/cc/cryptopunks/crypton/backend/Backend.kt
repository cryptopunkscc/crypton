package cc.cryptopunks.crypton.backend

import cc.cryptopunks.crypton.backend.internal.ConnectableBindingFactory
import cc.cryptopunks.crypton.backend.internal.Context
import cc.cryptopunks.crypton.backend.internal.ServiceFactory
import cc.cryptopunks.crypton.backend.internal.dropBindingInteractor
import cc.cryptopunks.crypton.backend.internal.getTopBindingSelector
import cc.cryptopunks.crypton.backend.internal.requestBindingInteractor
import cc.cryptopunks.crypton.context.AppCore
import cc.cryptopunks.crypton.context.Connectable
import cc.cryptopunks.crypton.context.Route
import cc.cryptopunks.crypton.module.AppDomainModule
import cc.cryptopunks.crypton.service.AppService
import cc.cryptopunks.crypton.util.Store
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class Backend(
    appCore: AppCore
) : AppCore by appCore {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Unconfined)

    private val stack: Store<List<Context>> = Store(emptyList())

    override val routeSys: Route.Sys = createRouteSys()

    override val navigate: Route.Navigate = Route.Navigate(routeSys)

    val request: (Route) -> Connectable.Binding = requestBindingInteractor(
        createService = ServiceFactory(this),
        createBinding = ConnectableBindingFactory(
            bindingStore = connectableBindingsStore,
            stack = stack
        )
    )

    val drop: () -> Connectable.Binding? = dropBindingInteractor(
        scope = scope,
        stack = stack
    )

    val top: () -> Connectable.Binding? = getTopBindingSelector(
        stack = stack
    )

    val appService: AppService by lazy {
        AppDomainModule(this).appService
    }
}
