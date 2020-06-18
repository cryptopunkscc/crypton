package cc.cryptopunks.crypton.interactor

import cc.cryptopunks.crypton.context.AppScope
import cc.cryptopunks.crypton.context.SessionScope
import kotlinx.coroutines.launch

fun AppScope.reconnectAccount() =
    sessionStore.get().values.map { session ->
        launch { session.reconnectIfNeeded() }
    }

private fun SessionScope.reconnectIfNeeded() {
    log.d("reconnecting: $address")
    if (isConnected()) interrupt()
    connect()
    if (!isAuthenticated()) login()
    initOmemo()
}
