package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.handle
import cc.cryptopunks.crypton.interactor.removeSessionScope
import kotlinx.coroutines.cancel

internal fun handleLogout() = handle { _, arg: Account.Service.Logout ->
    removeSessionScope(address) {
        disconnect()
        cancel(arg)
        log.d("Successful logout $address")
    }
}
