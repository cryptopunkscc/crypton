package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.context.AppScope
import cc.cryptopunks.crypton.context.handle
import cc.cryptopunks.crypton.interactor.removeSessionScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelChildren

internal fun AppScope.handleRemove() =
    handle<Account.Service.Remove> {
        removeSessionScope(address) {
            if (!deviceOnly) {
                removeAccount()
                log.d("Successfully removed $address from server")
            }
            interrupt()
            accountRepo.delete(address)
//            coroutineContext.cancelChildren()
            cancel()
            log.d("Successfully removed $address from local database")
        }
    }
