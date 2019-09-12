package cc.cryptopunks.crypton.presentation.viewmodel

import androidx.paging.RxPagedListBuilder
import cc.cryptopunks.crypton.domain.selector.ConversationDataSourceSelector
import cc.cryptopunks.crypton.module.ViewModelScope
import javax.inject.Inject

@ViewModelScope
class ConversationListViewModel @Inject constructor(
    conversationDataSource: ConversationDataSourceSelector,
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