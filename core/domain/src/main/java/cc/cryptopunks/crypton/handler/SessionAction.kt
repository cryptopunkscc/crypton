package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.handle
import cc.cryptopunks.crypton.interactor.interruptSessions
import cc.cryptopunks.crypton.interactor.reconnectSessions
import kotlinx.coroutines.joinAll

internal fun handleSessionAction() = handle { _, arg: Exec.Session ->
    log.d("handle $this")
    when (arg.action) {
        Exec.Session.Action.Reconnect -> reconnectSessions().joinAll()
        Exec.Session.Action.Interrupt -> interruptSessions()
    }
}
