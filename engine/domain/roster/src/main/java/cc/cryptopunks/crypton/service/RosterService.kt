package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.context.AppScope
import cc.cryptopunks.crypton.context.Connectable
import cc.cryptopunks.crypton.context.Connector
import cc.cryptopunks.crypton.context.Roster
import cc.cryptopunks.crypton.context.createHandlers
import cc.cryptopunks.crypton.context.dispatch
import cc.cryptopunks.crypton.context.plus
import cc.cryptopunks.crypton.handler.handleGetRosterItems
import cc.cryptopunks.crypton.handler.handleRosterItemsSubscription
import cc.cryptopunks.crypton.handler.handleSubscriptionAccept
import cc.cryptopunks.crypton.util.Store
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class RosterService(
    appScope: AppScope
) : AppScope by appScope,
    Connectable {

    override val coroutineContext = SupervisorJob() + Dispatchers.Unconfined

    override fun Connector.connect(): Job = launch {
        input.collect { handlers.dispatch(it, output) }
    }

    private val handlers by lazy {
        createHandlers {
            plus(handleGetRosterItems(rosterItemsStore))
            plus(handleRosterItemsSubscription(rosterItemsStore))
            plus(handleSubscriptionAccept(sessionStore))
        }
    }

    private val rosterItemsStore by lazy {
        Store(Roster.Service.Items(emptyList()))
    }
}

