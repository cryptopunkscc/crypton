package cc.cryptopunks.crypton.feature

import cc.cryptopunks.crypton.cliv2.command
import cc.cryptopunks.crypton.cliv2.param
import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.context.accountNet
import cc.cryptopunks.crypton.context.net
import cc.cryptopunks.crypton.create.handler
import cc.cryptopunks.crypton.create.inScope
import cc.cryptopunks.crypton.feature

internal fun connect() = feature(

    command = command(
        param(),
        name = "connect"
    ) { (account) ->
        Exec.Connect.inScope(account)
    },

    handler = handler { _, _: Exec.Connect ->
        net.run { if (!isConnected()) connect() }
        accountNet.run { if (!isAuthenticated()) login() }
    }
)
