package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import android.view.View
import cc.cryptopunks.crypton.context.Service
import cc.cryptopunks.crypton.service.ServiceManager
import cc.cryptopunks.crypton.util.ext.resolve

abstract class ServiceFragment :
    FeatureFragment() {

    private val serviceManager by lazy {
        appCore.resolve<ServiceManager.Core>().serviceManager
    }

    val binding by lazy {
        serviceManager.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.setRight(onCreatePresenter())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.setLeft(onCreateActor(view))
    }

    open fun onCreatePresenter(): Service? = null

    @Suppress("UNCHECKED_CAST")
    open fun onCreateActor(view: View): Service? = view as? Service

    override fun onDestroyView() {
        super.onDestroyView()
        binding.setLeft(null)
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceManager.remove(binding)
        binding.setRight(null)
    }
}