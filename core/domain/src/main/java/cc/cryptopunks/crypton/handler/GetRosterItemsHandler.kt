package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Roster
import cc.cryptopunks.crypton.handle

internal fun handleGetRosterItems() = handle { out, _: Roster.Service.GetItems ->
    rosterItems.get().out()
}
