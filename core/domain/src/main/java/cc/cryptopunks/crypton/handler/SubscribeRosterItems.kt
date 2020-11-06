package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Roster
import cc.cryptopunks.crypton.context.Subscribe
import cc.cryptopunks.crypton.handle
import cc.cryptopunks.crypton.selector.rosterItemStatesFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

internal fun handleRosterItemsSubscription() =
    handle { out, (_, account, inList): Subscribe.RosterItems ->
        rosterItemStatesFlow()
            .filterBy(account)
            .map { list -> list.sortedByDescending { item -> item.message.timestamp } }
            .map { Roster.Items(it) }
            .onEach { rosterItems { it } }
            .run {
                if (inList) collect(out)
                else collect { (items) ->
                    items.asFlow().collect(out)
                }
            }
    }

private fun Flow<List<Roster.Item>>.filterBy(account: Address?) =
    if (account == null) this else map { items ->
        items.filter { it.account == account }
    }
