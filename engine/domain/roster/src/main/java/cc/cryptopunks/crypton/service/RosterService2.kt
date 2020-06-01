package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.context.Connectable
import cc.cryptopunks.crypton.context.Connector
import cc.cryptopunks.crypton.context.Roster
import cc.cryptopunks.crypton.selector.RosterItemStateListFlowSelector
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class RosterService2 internal constructor(
    private val rosterListFlowSelector: RosterItemStateListFlowSelector
) : Connectable {
    override val coroutineContext: CoroutineContext = SupervisorJob() + Dispatchers.Unconfined

    override fun Connector.connect(): Job = launch {
        rosterListFlowSelector().map { Roster.Service2.Items(it) }.collect(output)
    }
}
