package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Roster
import cc.cryptopunks.crypton.context.handle
import cc.cryptopunks.crypton.util.Store
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

internal fun CoroutineScope.handleGetRosterItems(
    lastItems: Store<Roster.Service.Items>
) = handle<Roster.Service.GetItems> { output ->
    launch { output(lastItems.get()) }
}
