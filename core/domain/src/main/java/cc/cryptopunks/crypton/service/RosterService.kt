package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.context.Roster
import cc.cryptopunks.crypton.createHandlers
import cc.cryptopunks.crypton.handler.handleGetRosterItems
import cc.cryptopunks.crypton.handler.handleListJoinedRooms
import cc.cryptopunks.crypton.handler.handleListRooms
import cc.cryptopunks.crypton.handler.handleRosterItemsSubscription
import cc.cryptopunks.crypton.handler.handleSubscriptionAccept
import cc.cryptopunks.crypton.util.Store

fun rosterHandlers() = createHandlers {
    val store = Store(Roster.Service.Items(emptyList()))

    +handleGetRosterItems(store)
    +handleRosterItemsSubscription(store)
    +handleSubscriptionAccept()
    +handleListRooms()
    +handleListJoinedRooms()
}
