package cc.cryptopunks.crypton.interactor

import cc.cryptopunks.crypton.context.SessionScope
import kotlinx.coroutines.launch

suspend fun SessionScope.invokeSessionServices() {
    log.d("Invoke session services for $address")
    sessionBackgroundServices.forEach { service ->
        launch { service() }
    }
}
