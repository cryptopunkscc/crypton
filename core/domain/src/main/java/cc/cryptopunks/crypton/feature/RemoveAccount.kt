package cc.cryptopunks.crypton.feature

import cc.cryptopunks.crypton.cliv2.command
import cc.cryptopunks.crypton.cliv2.config
import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.context.account
import cc.cryptopunks.crypton.context.accountNet
import cc.cryptopunks.crypton.context.accountRepo
import cc.cryptopunks.crypton.context.net
import cc.cryptopunks.crypton.context.rootScope
import cc.cryptopunks.crypton.feature
import cc.cryptopunks.crypton.factory.handler
import cc.cryptopunks.crypton.inScope
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
        Exec.RemoveAccount().inScope(account)
    },

    handler = handler {_, arg: Exec.RemoveAccount ->
        val account = account
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
