package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.handle
import cc.cryptopunks.crypton.interactor.removeSessionScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.util.concurrent.CancellationException

internal fun handleRemoveAccount() = handle { _, arg: Exec.RemoveAccount ->
    removeSessionScope(address) {
        if (!arg.deviceOnly) {
            removeAccount()
            log.d("Successfully removed $address from server")
        }
        interrupt()
        accountRepo.delete(address)
        val session = this
        rootScope.launch {
            session.cancel(CancellationException(arg.toString()))
            log.d("Successfully removed $address from local database")
        }
    }
}
