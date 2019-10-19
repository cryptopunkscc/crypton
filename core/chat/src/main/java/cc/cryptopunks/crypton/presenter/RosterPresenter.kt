package cc.cryptopunks.crypton.presenter

import androidx.paging.PagedList
import cc.cryptopunks.crypton.actor.Actor
import cc.cryptopunks.crypton.entity.Chat
import cc.cryptopunks.crypton.entity.Message
import cc.cryptopunks.crypton.navigation.Navigation
import cc.cryptopunks.crypton.selector.RosterSelector
import cc.cryptopunks.crypton.util.ExecutorsComponent
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
    private val rosterFlow: RosterSelector,
    private val createRosterItem: RosterItemPresenter.Factory
) : Presenter<RosterPresenter.View> {

    @Singleton
    @dagger.Component(
        dependencies = [
            ExecutorsComponent::class,
            Navigation.Component::class,
            Chat.Repo::class,
            Message.Repo::class
        ]
    )
    interface Component : Presenter.Component<RosterPresenter>

    private val items = BroadcastChannel<PagedList<RosterItemPresenter>>(Channel.CONFLATED)

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