package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import android.view.View
import cc.cryptopunks.crypton.context.Actor
import cc.cryptopunks.crypton.context.Connectable
import cc.cryptopunks.crypton.context.Connector
import cc.cryptopunks.crypton.service.ConnectableBuffer
import cc.cryptopunks.crypton.service.ServiceBinding
import cc.cryptopunks.crypton.service.ServiceManager
import cc.cryptopunks.crypton.util.ext.resolve
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

abstract class ServiceFragment :
    FeatureFragment(),
    Connectable,
    Connector {

    private val serviceManager: ServiceManager by lazy {
        appCore.resolve<ServiceManager.Core>().serviceManager
    }

    protected val binding: ServiceBinding by lazy {
        serviceManager.createBinding()
    }

    override val input: Flow<Any> get() = binding.input
    override val output: suspend (Any) -> Unit get() = binding.output

    protected val viewProxy = ConnectableBuffer(SupervisorJob() + Dispatchers.IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding + viewProxy
        binding + onCreatePresenter()
    }

    protected open fun onCreatePresenter(): Connectable? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewProxy.service = onCreateActor(view)
    }

    @Suppress("UNCHECKED_CAST")
    protected open fun onCreateActor(view: View): Connectable? = view as? Connectable

    override fun onStart() {
        super.onStart()
        launch { Actor.Start.out() }
    }

    override fun onStop() {
        super.onStop()
        launch { Actor.Stop.out() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewProxy.service = null
        binding.minus<Actor>()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.cancel()
        serviceManager.remove(binding)
    }
}