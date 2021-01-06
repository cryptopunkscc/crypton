package cc.cryptopunks.crypton.feature

import cc.cryptopunks.crypton.cliv2.command
import cc.cryptopunks.crypton.cliv2.named
import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.context.Password
import cc.cryptopunks.crypton.context.address
import cc.cryptopunks.crypton.factory.handler
import cc.cryptopunks.crypton.feature
import cc.cryptopunks.crypton.interactor.addAccount
import cc.cryptopunks.crypton.interactor.connectAccount

internal fun registerAccount() = feature(

    command = command(
        named("account").copy(name = "local@domain", description = "XMPP account address"),
        named("password").copy(name = "****", description = "XMPP account password"),
        name = "register",
        description = "Create new xmpp account on server."
    ) { (account, password) ->
        Exec.Register(Account(address(account), Password(password)))
    },

    handler = handler { out, (account): Exec.Register ->
        account.connectAccount(out) {
            addAccount(
                account = account,
                register = true,
                insert = true
            )
        }
    }
)
