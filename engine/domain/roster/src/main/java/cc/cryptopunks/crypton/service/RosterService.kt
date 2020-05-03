package cc.cryptopunks.crypton.service

import androidx.paging.PagedList
import cc.cryptopunks.crypton.context.Connectable
import cc.cryptopunks.crypton.context.Connector
import cc.cryptopunks.crypton.context.Roster
import cc.cryptopunks.crypton.selector.RosterSelector
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class RosterService internal constructor(
    private val rosterFlow: RosterSelector,
    private val createRosterItem: RosterItemService.Factory
) : Roster.Service {

    private val log = typedLog()

    override val coroutineContext = SupervisorJob() + Dispatchers.Main

    private var items: Roster.Service.Items? = null

    override fun Connector.connect() = launch {
        log.d("bind")
        launch {
            input.collect {
                when (it) {
                    is Roster.Service.GetItems -> items?.out()
                }
            }
        }
        launch {
            rosterFlow { createRosterItem(it) }
                .map { Roster.Service.Items(it as PagedList<Connectable>) }
                .onEach {
                    log.d("next: ${it.items.size}")
                    items = it
                }
                .collect(output)
        }
    }
}
