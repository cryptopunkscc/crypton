package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.context.AppScope
import cc.cryptopunks.crypton.context.Roster
import cc.cryptopunks.crypton.context.createHandlers
import cc.cryptopunks.crypton.context.plus
import cc.cryptopunks.crypton.handler.handleGetRosterItems
import cc.cryptopunks.crypton.handler.handleRosterItemsSubscription
import cc.cryptopunks.crypton.handler.handleSubscriptionAccept
import cc.cryptopunks.crypton.util.Store
import cc.cryptopunks.crypton.util.service

fun rosterService(scope: AppScope) = service(scope) {
    rosterHandlers()
}

fun AppScope.rosterHandlers() = createHandlers {
    val store = Store(Roster.Service.Items(emptyList()))

    plus(handleGetRosterItems(store))
    plus(handleRosterItemsSubscription(store))
    plus(handleSubscriptionAccept())
}
