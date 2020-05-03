package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import android.view.View
import cc.cryptopunks.crypton.context.Actor
import cc.cryptopunks.crypton.context.Connectable
import cc.cryptopunks.crypton.context.Connector
import cc.cryptopunks.crypton.service.ConnectableBuffer
import cc.cryptopunks.crypton.service.createBinding
import cc.cryptopunks.crypton.service.remove
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow

abstract class ServiceFragment :
    FeatureFragment(),
    Connectable,
    Connector {

    protected val binding: Connectable.Binding by lazy {
        runBlocking { appCore.connectableBindingsStore.createBinding() }
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
    }

    override fun onDestroy() {
        super.onDestroy()
        runBlocking {
            viewProxy.cancel()
            binding.cancel()
            log.d("binding canceled")
            appCore.connectableBindingsStore.remove(binding)
            log.d("binding removed")
        }
        log.d("destroyed")
    }
}
