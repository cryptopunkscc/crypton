package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.context.AppScope
import cc.cryptopunks.crypton.context.handle
import cc.cryptopunks.crypton.interactor.removeSessionScope
import kotlinx.coroutines.cancel

internal fun AppScope.handleRemove() =
    handle<Account.Service.Remove> {
        removeSessionScope(address) {
            if (!deviceOnly) {
                removeAccount()
                log.d("Successfully removed $address from server")
            }
            accountRepo.delete(address)
            cancel()
            log.d("Successfully removed $address from local database")
        }
    }
