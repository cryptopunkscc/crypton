package cc.cryptopunks.crypton

import androidx.paging.PagedList
import cc.cryptopunks.crypton.context.Service
import cc.cryptopunks.crypton.selector.RosterSelector
import cc.cryptopunks.crypton.util.ext.map
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class RosterService @Inject constructor(
    private val rosterFlow: RosterSelector,
    private val createRosterItem: RosterItemService.Factory
) : Service {

    data class Items(val items: PagedList<Service>)

    private val log = typedLog()

    override val coroutineContext = SupervisorJob() + Dispatchers.IO

    override fun Service.Connector.connect() = launch {
        log.d("bind")
        rosterFlow { createRosterItem(it) as Service }
            .map(RosterService::Items)
            .collect {
                log.d("next: ${it.items.size}")
                it.out()
            }
    }

    interface Core {
        val rosterService: RosterService
    }
}