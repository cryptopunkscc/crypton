package cc.cryptopunks.crypton.feature.chat.presenter

import androidx.paging.PagedList
import cc.cryptopunks.crypton.feature.chat.interactor.LoadMessagesInteractor
import cc.cryptopunks.crypton.feature.chat.selector.RosterSelector
import cc.cryptopunks.crypton.util.Presenter
import kotlinx.coroutines.flow.collect
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RosterPresenter @Inject constructor(
    private val loadMessages: LoadMessagesInteractor,
    private val conversationFlow: RosterSelector,
    private val createRosterItem: RosterItemPresenter.Factory
) : Presenter<RosterPresenter.View> {

    override suspend fun invoke() =
        loadMessages()

    override suspend fun View.invoke() =
        conversationFlow(createRosterItem).collect(setList)

    interface View {
        val setList: suspend (PagedList<RosterItemPresenter>) -> Unit
    }
}