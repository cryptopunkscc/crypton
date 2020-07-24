package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Main
import cc.cryptopunks.crypton.handle
import cc.cryptopunks.crypton.interactor.interruptSessions
import cc.cryptopunks.crypton.interactor.reconnectSessions
import kotlinx.coroutines.joinAll

internal fun handleSessionAction() = handle { _, arg: Main.Command.Session ->
    log.d("handle $this")
    when (arg.status) {
        Main.Command.Session.Action.Reconnect -> reconnectSessions().joinAll()
        Main.Command.Session.Action.Interrupt -> interruptSessions()
    }
}
