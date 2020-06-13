package cc.cryptopunks.crypton.service

import androidx.paging.PagedList
import cc.cryptopunks.crypton.context.Connectable
import cc.cryptopunks.crypton.context.Connector
import cc.cryptopunks.crypton.context.Roster
import cc.cryptopunks.crypton.selector.RosterPagedListSelector
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class RosterPagedService internal constructor(
    private val rosterPagedListFlow: RosterPagedListSelector,
    private val createRosterItem: RosterItemService.Factory
) : Connectable {

    private val log = typedLog()

    override val coroutineContext = SupervisorJob() + Dispatchers.Unconfined

    private var pagedItems: Roster.Service.PagedItems? = null

    override fun Connector.connect() = launch {
        log.d("Start")
        launch {
            input.collect {
                when (it) {
                    is Roster.Service.GetPagedItems -> pagedItems?.out()
                }
            }
        }
        launch {
            rosterPagedListFlow { createRosterItem(it) }
                .map { Roster.Service.PagedItems(it as PagedList<Connectable>) }
                .onEach {
                    log.d("Received ${it.items.size} items")
                    pagedItems = it
                }
                .collect(output)
        }
    }
}
