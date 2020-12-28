package cc.cryptopunks.crypton.feature

import cc.cryptopunks.crypton.cliv2.command
import cc.cryptopunks.crypton.context.Get
import cc.cryptopunks.crypton.context.rosterItems
import cc.cryptopunks.crypton.feature
import cc.cryptopunks.crypton.factory.handler
internal fun getRosterItems() = feature(

    command = command(
        name = "roster",
        description = "Get roster for all accounts."
    ) {
        Get.RosterItems
    },

    handler = handler {out, _: Get.RosterItems ->
        rosterItems.get().out()
    }
)
