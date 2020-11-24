package cc.cryptopunks.crypton.feature

import cc.cryptopunks.crypton.cliv2.command
import cc.cryptopunks.crypton.context.Get
import cc.cryptopunks.crypton.feature

internal fun getRosterItems() = feature(

    command = command(
        name = "roster",
        description = "Get roster for all accounts."
    ) {
        Get.RosterItems
    },

    handler = { out, _: Get.RosterItems ->
        rosterItems.get().out()
    }
)
