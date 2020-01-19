package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import android.view.View
import cc.cryptopunks.crypton.context.Service
import cc.cryptopunks.crypton.service.ServiceBinding
import cc.cryptopunks.crypton.service.ServiceManager
import cc.cryptopunks.crypton.util.ext.resolve
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow

abstract class ServiceFragment :
    FeatureFragment(),
    Service,
    Service.Connector {

    private val serviceManager: ServiceManager by lazy {
        appCore.resolve<ServiceManager.Core>().serviceManager
    }

    val binding: ServiceBinding by lazy {
        serviceManager.createBinding()
    }

    override val input: Flow<Any> get() = binding.input

    override val output: suspend (Any) -> Unit get() = binding.output

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding + onCreatePresenter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.slot1 = onCreateActor(view)
    }

    open fun onCreatePresenter(): Service? = null

    @Suppress("UNCHECKED_CAST")
    open fun onCreateActor(view: View): Service? = view as? Service

    override fun onDestroyView() {
        super.onDestroyView()
        binding.apply {
            slot1?.cancel()
            slot1 = null
        }
        binding.minus<Service.Actor>()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.cancel()
        serviceManager.remove(binding)
    }
}