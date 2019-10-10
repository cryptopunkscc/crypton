package cc.cryptopunks.crypton.feature.chat.presenter

import androidx.paging.PagedList
import cc.cryptopunks.crypton.feature.chat.interactor.LoadMessagesInteractor
import cc.cryptopunks.crypton.feature.chat.selector.RosterSelector
import cc.cryptopunks.crypton.util.Presenter
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
    private val loadMessages: LoadMessagesInteractor,
    private val rosterFlow: RosterSelector,
    private val createRosterItem: RosterItemPresenter.Factory
) : Presenter<RosterPresenter.View> {

    private val items = BroadcastChannel<PagedList<RosterItemPresenter>>(Channel.CONFLATED)

    override suspend fun invoke() = coroutineScope {
        loadMessages()
        launch { rosterFlow(createRosterItem).collect(items::send) }
    }

    override suspend fun View.invoke() = items.asFlow().collect(setList)

    interface View {
        val setList: suspend (PagedList<RosterItemPresenter>) -> Unit
    }
}