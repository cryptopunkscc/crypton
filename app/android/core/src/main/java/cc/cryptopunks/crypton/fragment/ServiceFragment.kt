package cc.cryptopunks.crypton.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import cc.cryptopunks.crypton.Actor
import cc.cryptopunks.crypton.Connectable
import cc.cryptopunks.crypton.connectableBindingsStore
import cc.cryptopunks.crypton.context.ActivityResult
import cc.cryptopunks.crypton.context.PermissionsResult
import cc.cryptopunks.crypton.createBinding
import cc.cryptopunks.crypton.minus
import cc.cryptopunks.crypton.remove
import cc.cryptopunks.crypton.service
import cc.cryptopunks.crypton.serviceName
import kotlinx.coroutines.runBlocking

abstract class ServiceFragment :
    FeatureFragment(),
    Connectable {

    protected val binding: Connectable.Binding by lazy {
        runBlocking { rootScope.connectableBindingsStore.createBinding() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding + onCreateService()
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        binding.send(
            ActivityResult(
                requestCode = requestCode,
                resultCode = resultCode,
                intent = data ?: Intent()
            )
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        binding.send(
            PermissionsResult(
                requestCode = requestCode,
                permissions = permissions.toList(),
                grantResults = grantResults.toList()
            )
        )
    }

    protected open fun onCreateService(): Connectable? = rootScope.service(serviceName)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding + onCreateActor(view)
    }

    @Suppress("UNCHECKED_CAST")
    protected open fun onCreateActor(view: View): Actor? = view as? Actor

    override fun onDestroyView() {
        super.onDestroyView()
        binding.minus<Actor>()
    }

    override fun onDestroy() {
        super.onDestroy()
        runBlocking {
            binding.cancel()
            log.d { "binding canceled" }
            rootScope.connectableBindingsStore.remove(binding)
            log.d { "binding removed" }
        }
        log.d { "destroyed" }
    }
}
