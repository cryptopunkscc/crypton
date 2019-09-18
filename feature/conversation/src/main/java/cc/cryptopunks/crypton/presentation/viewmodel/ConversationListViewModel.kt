package cc.cryptopunks.crypton.presentation.viewmodel

import cc.cryptopunks.crypton.domain.interactor.LoadMessagesInteractor
import cc.cryptopunks.crypton.domain.selector.ConversationPagedListSelector
import cc.cryptopunks.crypton.module.ViewModelScope
import kotlinx.coroutines.Job
import javax.inject.Inject

@ViewModelScope
class ConversationListViewModel @Inject constructor(
    private val loadMessages: LoadMessagesInteractor,
    private val conversationListFlow: ConversationPagedListSelector,
    private val createConversationItem: ConversationItemViewModel.Factory
) : () -> Job by loadMessages {

    val pagedItems get() = conversationListFlow(createConversationItem)
}