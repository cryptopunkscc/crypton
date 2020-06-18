package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.AppScope
import cc.cryptopunks.crypton.context.Network
import cc.cryptopunks.crypton.context.handle
import cc.cryptopunks.crypton.interactor.interruptSessions
import cc.cryptopunks.crypton.interactor.reconnectAccount
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch

fun AppScope.handleNetworkStatus() = handle<Network.Status> {
    launch {
        when (this@handle) {
            is Network.Status.Available,
            is Network.Status.Changed -> reconnectAccount().joinAll()
            is Network.Status.Unavailable -> interruptSessions()
        }
    }
}
