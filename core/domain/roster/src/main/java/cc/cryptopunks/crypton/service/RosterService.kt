package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.context.AppScope
import cc.cryptopunks.crypton.context.Roster
import cc.cryptopunks.crypton.createHandlers
import cc.cryptopunks.crypton.handler.handleGetRosterItems
import cc.cryptopunks.crypton.handler.handleRosterItemsSubscription
import cc.cryptopunks.crypton.handler.handleSubscriptionAccept
import cc.cryptopunks.crypton.HandlerRegistryFactory
import cc.cryptopunks.crypton.util.Store

val rosterHandlers: HandlerRegistryFactory<AppScope> = {
    createHandlers {
        val store = Store(Roster.Service.Items(emptyList()))

        +handleGetRosterItems(store)
        +handleRosterItemsSubscription(store)
        +handleSubscriptionAccept()
    }
}
