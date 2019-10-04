package cc.cryptopunks.crypton.presentation.viewmodel

import cc.cryptopunks.crypton.domain.interactor.LoadMessagesInteractor
import cc.cryptopunks.crypton.domain.selector.RosterSelector
import cc.cryptopunks.crypton.dagger.ViewModelScope
import kotlinx.coroutines.Job
import javax.inject.Inject

@ViewModelScope
class RosterViewModel @Inject constructor(
    private val loadMessages: LoadMessagesInteractor,
    private val conversationFlow: RosterSelector,
    private val createRosterItem: RosterItemViewModel.Factory
) : () -> Job by loadMessages {

    val pagedItems get() = conversationFlow(createRosterItem)
}