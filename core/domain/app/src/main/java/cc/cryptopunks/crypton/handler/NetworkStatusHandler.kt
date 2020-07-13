package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.AppScope
import cc.cryptopunks.crypton.context.Network
import cc.cryptopunks.crypton.handle
import cc.cryptopunks.crypton.interactor.interruptSessions
import cc.cryptopunks.crypton.interactor.reconnectSessions
import kotlinx.coroutines.joinAll

fun AppScope.handleNetworkStatus() = handle<Network.Status> {
    log.d("handle $this")
    when (this@handle) {
        is Network.Status.Available,
        is Network.Status.Changed -> reconnectSessions().joinAll()
        is Network.Status.Unavailable -> interruptSessions()
    }
}
