package cc.cryptopunks.crypton.interactor

import cc.cryptopunks.crypton.context.*
import kotlinx.coroutines.delay

class SessionEventHandler(
    private val session: Session,
    private val networkSys: Network.Sys
) {
    suspend operator fun invoke(event: Api.Event) {
        when (event) {
            is Net.Disconnected -> if (event.hasError) session.scope.launch {
                waitForNetworkConnection()
                if (!session.isConnected())
                    if (networkSys.status.isConnected)
                        session.reconnect() else
                        session.interrupt()
            }
        }
    }

    private suspend fun waitForNetworkConnection() = delay(2000)

    private fun Session.reconnect() {
        connect()
        if (!isAuthenticated()) login()
        initOmemo()
    }
}
