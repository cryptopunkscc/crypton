package cc.cryptopunks.crypton.feature.chat.viewmodel

import cc.cryptopunks.crypton.feature.chat.interactor.LoadMessagesInteractor
import cc.cryptopunks.crypton.feature.chat.selector.RosterSelector
import kotlinx.coroutines.Job
import javax.inject.Inject

class RosterViewModel @Inject constructor(
    private val loadMessages: LoadMessagesInteractor,
    private val conversationFlow: RosterSelector,
    private val createRosterItem: RosterItemViewModel.Factory
) : () -> Job by loadMessages {

    val pagedItems get() = conversationFlow(createRosterItem)
}