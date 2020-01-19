package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import android.view.View
import cc.cryptopunks.crypton.context.Service
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
    Service,
    Service.Connector {

    private val serviceManager: ServiceManager by lazy {
        appCore.resolve<ServiceManager.Core>().serviceManager
    }

    protected val binding: ServiceBinding by lazy {
        serviceManager.createBinding()
    }

    protected val viewProxy = ConnectableBuffer(SupervisorJob() + Dispatchers.IO)

    override val input: Flow<Any> get() = binding.input

    override val output: suspend (Any) -> Unit get() = binding.output

    protected open fun onCreatePresenter(): Service? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding + viewProxy
        binding + onCreatePresenter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewProxy.service = onCreateActor(view)
    }

    @Suppress("UNCHECKED_CAST")
    protected open fun onCreateActor(view: View): Service? = view as? Service

    override fun onStart() {
        super.onStart()
        launch { Service.Actor.Start.out() }
    }

    override fun onStop() {
        super.onStop()
        launch { Service.Actor.Stop.out() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewProxy.service = null
        binding.minus<Service.Actor>()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.cancel()
        serviceManager.remove(binding)
    }
}