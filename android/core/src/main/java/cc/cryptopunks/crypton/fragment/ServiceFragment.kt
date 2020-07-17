package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import android.view.View
import cc.cryptopunks.crypton.Actor
import cc.cryptopunks.crypton.Connectable
import cc.cryptopunks.crypton.createBinding
import cc.cryptopunks.crypton.minus
import cc.cryptopunks.crypton.remove
import kotlinx.coroutines.runBlocking

abstract class ServiceFragment :
    FeatureFragment(),
    Connectable {

    protected val binding: Connectable.Binding by lazy {
        runBlocking { appScope.connectableBindingsStore.createBinding() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding + onCreateService()
    }

    protected open fun onCreateService(): Connectable? = appScope

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding + onCreateActor(view)
    }

    @Suppress("UNCHECKED_CAST")
    protected open fun onCreateActor(view: View): Actor? = view as? Actor

    override fun onStart() {
        super.onStart()
        binding.send(Actor.Start)
    }

    override fun onStop() {
        super.onStop()
        binding.send(Actor.Stop)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.minus<Actor>()
    }

    override fun onDestroy() {
        super.onDestroy()
        runBlocking {
            binding.cancel()
            log.d("binding canceled")
            appScope.connectableBindingsStore.remove(binding)
            log.d("binding removed")
        }
        log.d("destroyed")
    }
}
