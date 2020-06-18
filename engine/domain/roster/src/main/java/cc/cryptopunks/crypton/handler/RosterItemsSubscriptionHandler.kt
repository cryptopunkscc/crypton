package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.AppScope
import cc.cryptopunks.crypton.context.Roster
import cc.cryptopunks.crypton.context.handle
import cc.cryptopunks.crypton.selector.rosterItemStatesFlow
import cc.cryptopunks.crypton.util.Store
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

internal fun AppScope.handleRosterItemsSubscription(
    lastItems: Store<Roster.Service.Items>
) = handle<Roster.Service.SubscribeItems> { output ->
    launch {
        rosterItemStatesFlow()
            .map { Roster.Service.Items(it) }
            .onEach { lastItems { it } }
            .collect(output)
    }
}
