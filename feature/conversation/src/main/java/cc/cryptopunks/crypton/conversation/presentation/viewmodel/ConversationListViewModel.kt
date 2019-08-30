package cc.cryptopunks.crypton.conversation.presentation.viewmodel

import androidx.paging.RxPagedListBuilder
import cc.cryptopunks.crypton.conversation.domain.query.ConversationDataSource
import cc.cryptopunks.crypton.module.ViewModelScope
import javax.inject.Inject

@ViewModelScope
class ConversationListViewModel @Inject constructor(
    conversationDataSource: ConversationDataSource,
    createConversationItem: ConversationItemViewModel.Factory
) {

    val pagedItems = conversationDataSource()
        .map(createConversationItem)
        .let { RxPagedListBuilder(it, DEFAULT_PAGE_SIZE) }
        .buildObservable()

    private companion object {
        const val DEFAULT_PAGE_SIZE = 20
    }
}