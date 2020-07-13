package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.context.AppScope
import cc.cryptopunks.crypton.handle
import cc.cryptopunks.crypton.interactor.removeSessionScope
import kotlinx.coroutines.cancel

internal fun AppScope.handleLogout() =
    handle<Account.Service.Logout> {
        removeSessionScope(address) {
            disconnect()
            cancel()
            log.d("Successful logout $address")
        }
    }
