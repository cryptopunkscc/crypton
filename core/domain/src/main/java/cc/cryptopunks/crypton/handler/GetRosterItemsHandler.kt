package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Roster
import cc.cryptopunks.crypton.handle
import cc.cryptopunks.crypton.util.Store

internal fun handleGetRosterItems(
    lastItems: Store<Roster.Service.Items>
) =
    handle { out, _: Roster.Service.GetItems ->
        out(lastItems.get())
    }
