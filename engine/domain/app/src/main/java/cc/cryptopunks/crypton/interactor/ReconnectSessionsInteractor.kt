package cc.cryptopunks.crypton.interactor

import cc.cryptopunks.crypton.context.Session
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch

internal class ReconnectSessionsInteractor(
    private val sessionStore: Session.Store
) {
    private val log = typedLog()

    suspend operator fun invoke() = coroutineScope {
        sessionStore.get().values.map { session ->
            launch { session.reconnectIfNeeded() }
        }.joinAll()
    }

    private fun Session.reconnectIfNeeded() {
        log.d("reconnecting: $address")
        if (isConnected()) interrupt()
        connect()
        if (!isAuthenticated()) login()
        initOmemo()
    }
}
