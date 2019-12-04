package cc.cryptopunks.crypton.presenter

import androidx.paging.PagedList
import cc.cryptopunks.crypton.context.Feature
import cc.cryptopunks.crypton.context.Service
import cc.cryptopunks.crypton.selector.RosterSelector
import cc.cryptopunks.crypton.util.ext.map
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class RosterService @Inject constructor(
    override val scope: Feature.Scope,
    private val rosterFlow: RosterSelector,
    private val createRosterItem: RosterItemPresenter.Factory
) : Service.Abstract() {

    private val log = typedLog()

    private var rosterItems: Output.RosterItemList? = null

    override fun onInvoke() {
        scope.launch {
            rosterFlow(createRosterItem)
                .map(Output::RosterItemList)
                .onEach { rosterItems = it }
                .onEach { log.d("obtained ${it.items.size} chat items") }
                .collect(out)
        }
    }

    override suspend fun onCollect(collector: FlowCollector<Any>) {
        rosterItems?.let { collector.emit(it) }
    }

    interface Output {
        data class RosterItemList(val items: PagedList<RosterItemPresenter>) : Output
    }

    interface Core {
        val rosterService: RosterService
    }
}