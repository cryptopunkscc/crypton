package cc.cryptopunks.crypton.feature

import cc.cryptopunks.crypton.cliv2.command
import cc.cryptopunks.crypton.cliv2.config
import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.feature
import cc.cryptopunks.crypton.inContext
import cc.cryptopunks.crypton.interactor.removeSessionScope
import cc.cryptopunks.crypton.util.logger.log
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

internal fun removeAccount() = feature(

    command = command(
        config("account"),
        name = "delete account",
        description = "Delete account."
    ) { (account) ->
        Exec.RemoveAccount().inContext(account)
    },

    handler = { _, arg: Exec.RemoveAccount ->
        removeSessionScope(account.address) {
            if (!arg.deviceOnly) {
                accountNet.removeAccount()
                log.d { "Successfully removed $account from server" }
            }
            net.interrupt()
            accountRepo.delete(account.address)
            val session = this
            rootScope.launch {
                session.cancel(arg.toString())
                log.d { "Successfully removed $account from local database" }
            }
        }
    }
)
