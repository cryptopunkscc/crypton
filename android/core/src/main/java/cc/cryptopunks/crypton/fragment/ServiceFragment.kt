package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import android.view.View
import cc.cryptopunks.crypton.context.Service
import cc.cryptopunks.crypton.service.ServiceBindingManager
import cc.cryptopunks.crypton.util.ext.resolve
import kotlinx.coroutines.cancel

abstract class ServiceFragment :
    FeatureFragment() {

    private val serviceManager by lazy {
        appCore.resolve<ServiceBindingManager.Core>().serviceBindingManager
    }

    val binding by lazy {
        serviceManager.createBinding()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.right = onCreatePresenter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.left = onCreateActor(view)
    }

    open fun onCreatePresenter(): Service? = null

    @Suppress("UNCHECKED_CAST")
    open fun onCreateActor(view: View): Service? = view as? Service

    override fun onDestroyView() {
        super.onDestroyView()
        binding.apply {//            ConnectedService(createRosterItem(it)) as Service

            left?.cancel()
            left = null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.apply {
            right?.cancel()
            right = null
        }
        serviceManager.remove(binding)
    }
}