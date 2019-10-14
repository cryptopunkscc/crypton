package cc.cryptopunks.crypton.feature.chat.presenter

import androidx.paging.PagedList
import cc.cryptopunks.crypton.actor.Actor
import cc.cryptopunks.crypton.feature.chat.interactor.LoadMessagesInteractor
import cc.cryptopunks.crypton.feature.chat.selector.RosterSelector
import cc.cryptopunks.crypton.presenter.Presenter
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RosterPresenter @Inject constructor(
    loadMessages: LoadMessagesInteractor,
    private val rosterFlow: RosterSelector,
    private val createRosterItem: RosterItemPresenter.Factory
) : Presenter<RosterPresenter.View> {

    private val items = BroadcastChannel<PagedList<RosterItemPresenter>>(Channel.CONFLATED)

    init {
        loadMessages()
    }

    override suspend fun invoke(): Any = coroutineScope {
        launch { rosterFlow(createRosterItem).collect(items::send) }
    }

    override suspend fun View.invoke() = coroutineScope {
        launch { items.asFlow().collect(setList) }
    }

    interface View : Actor {
        val setList: suspend (PagedList<RosterItemPresenter>) -> Unit
    }
}