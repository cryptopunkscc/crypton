package cc.cryptopunks.crypton.feature

import cc.cryptopunks.crypton.cliv2.command
import cc.cryptopunks.crypton.cliv2.config
import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.context.deviceNet
import cc.cryptopunks.crypton.create.handler
import cc.cryptopunks.crypton.create.inScope
import cc.cryptopunks.crypton.feature

internal fun purgeDeviceList() = feature(

    command = command(
        config("account"),
        name = "purge devices",
        description = "Purge device list for account"
    ) { (account) ->
        Exec.PurgeDeviceList.inScope(account)
    },

    handler = handler { _, _: Exec.PurgeDeviceList ->
        deviceNet.purgeDeviceList()
    }
)
