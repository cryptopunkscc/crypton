package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Get
import cc.cryptopunks.crypton.handle

internal fun handleGetRosterItems() = handle { out, _: Get.RosterItems ->
    rosterItems.get().out()
}
