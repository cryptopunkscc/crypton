package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.handle
import cc.cryptopunks.crypton.interactor.removeSessionScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.util.concurrent.CancellationException

internal fun handleRemoveAccount() = handle { _, arg: Account.Service.Remove ->
    removeSessionScope(address) {
        if (!arg.deviceOnly) {
            removeAccount()
            log.d("Successfully removed $address from server")
        }
        interrupt()
        accountRepo.delete(address)
        val session = this
        appScope.launch {
            session.cancel(CancellationException(arg.toString()))
            log.d("Successfully removed $address from local database")
        }
    }
}
