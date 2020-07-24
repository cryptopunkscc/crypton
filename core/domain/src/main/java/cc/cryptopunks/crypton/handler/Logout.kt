package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.handle
import cc.cryptopunks.crypton.interactor.removeSessionScope
import kotlinx.coroutines.cancel
import java.util.concurrent.CancellationException

internal fun handleLogout() = handle { _, arg: Exec.Disconnect ->
    removeSessionScope(address) {
        disconnect()
        cancel(CancellationException(arg.toString()))
        log.d("Successful logout $address")
    }
}
