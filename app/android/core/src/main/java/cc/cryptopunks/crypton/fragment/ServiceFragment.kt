package cc.cryptopunks.crypton.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import cc.cryptopunks.crypton.Connectable
import cc.cryptopunks.crypton.Service
import cc.cryptopunks.crypton.connect
import cc.cryptopunks.crypton.context.ActivityResult
import cc.cryptopunks.crypton.context.PermissionsResult
import cc.cryptopunks.crypton.factory.connector
import cc.cryptopunks.crypton.service.start
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

abstract class ServiceFragment :
    FeatureFragment() {

    protected val service = Service()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onCreateService()
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        service.input.offer(
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
        service.input.offer(
            PermissionsResult(
                requestCode = requestCode,
                permissions = permissions.toList(),
                grantResults = grantResults.toList()
            )
        )
    }

    protected open fun onCreateService() {
        launch { service.start() }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onCreateActor(view)
    }

    @Suppress("UNCHECKED_CAST")
    protected open fun onCreateActor(view: View) {
        (view as? Connectable)?.connect(service.connector())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (view as? CoroutineScope)?.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }
}
