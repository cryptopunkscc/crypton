package cc.cryptopunks.crypton.presenter

import androidx.paging.PagedList
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.Presenter
import cc.cryptopunks.crypton.selector.RosterSelector
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class RosterPresenter @Inject constructor(
    private val rosterFlow: RosterSelector,
    private val createRosterItem: RosterItemPresenter.Factory
) :
    Presenter<RosterPresenter.Actor>,
    Message.Consumer {

    private val items = BroadcastChannel<PagedList<RosterItemPresenter>>(Channel.CONFLATED)

    override suspend fun invoke(): Any = coroutineScope {
        launch { rosterFlow(createRosterItem).collect(items::send) }
    }

    override suspend fun Actor.invoke() = coroutineScope {
        launch { items.asFlow().collect(setList) }
    }

    override fun canConsume(message: Message) = true

    interface Actor {
        val setList: suspend (PagedList<RosterItemPresenter>) -> Unit
    }

    interface Core {
        val rosterPresenter: RosterPresenter
    }
}