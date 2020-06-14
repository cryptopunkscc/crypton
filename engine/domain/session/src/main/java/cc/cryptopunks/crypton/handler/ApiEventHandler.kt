package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Api
import cc.cryptopunks.crypton.context.Net
import cc.cryptopunks.crypton.context.Network
import cc.cryptopunks.crypton.context.Session
import cc.cryptopunks.crypton.context.handle
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

internal fun Session.handleApiEvent(
    networkSys: Network.Sys
) = handle<Api.Event> {
    scope.launch {
        when (this@handle) {
            is Net.Disconnected -> if (hasError) scope.launch {
                delay(2000) // wait for network connection
                if (!isConnected())
                    if (networkSys.status.isConnected)
                        reconnect() else
                        interrupt()
            }
        }
    }
}

private fun Session.reconnect() {
    connect()
    if (!isAuthenticated()) login()
    initOmemo()
}
