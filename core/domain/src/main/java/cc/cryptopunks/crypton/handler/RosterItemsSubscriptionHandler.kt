package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Roster
import cc.cryptopunks.crypton.handle
import cc.cryptopunks.crypton.selector.rosterItemStatesFlow
import cc.cryptopunks.crypton.util.Store
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

internal fun handleRosterItemsSubscription(
    lastItems: Store<Roster.Service.Items>
) =
    handle { out, (_, account): Roster.Service.SubscribeItems ->
        rosterItemStatesFlow()
            .filterBy(account)
            .map { list -> list.sortedByDescending { item -> item.message.timestamp } }
            .map { Roster.Service.Items(it) }
            .onEach { lastItems { it } }
            .collect(out)
    }

private fun Flow<List<Roster.Item>>.filterBy(account: Address?) =
    if (account == null) this else map { items ->
        items.filter { it.account == account }
    }
