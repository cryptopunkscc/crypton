package cc.cryptopunks.crypton

import androidx.paging.PagedList
import cc.cryptopunks.crypton.context.Connector
import cc.cryptopunks.crypton.context.Roster
import cc.cryptopunks.crypton.context.Service
import cc.cryptopunks.crypton.selector.RosterSelector
import cc.cryptopunks.crypton.util.ext.map
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class RosterService @Inject constructor(
    private val rosterFlow: RosterSelector,
    private val createRosterItem: RosterItemService.Factory
) : Roster.Service {

    data class Items(val items: PagedList<Service>)

    private val log = typedLog()

    override val coroutineContext = SupervisorJob() + Dispatchers.Main

    private var items: Items? = null

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
            rosterFlow { createRosterItem(it) as Service }
                .map(RosterService::Items)
                .onEach {
                    log.d("next: ${it.items.size}")
                    items = it
                }
                .collect(output)
        }
    }

    interface Core {
        val rosterService: RosterService
    }
}