package cc.cryptopunks.crypton.domain.selector

import androidx.paging.PagedList
import cc.cryptopunks.crypton.entity.Conversation
import cc.cryptopunks.crypton.util.CreatePagedList
import cc.cryptopunks.crypton.util.asFlow
import cc.cryptopunks.crypton.util.pagedListConfig
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

data class ConversationPagedListSelector @Inject constructor(
    private val dao: Conversation.Dao
) {
    operator fun <T> invoke(mapper: Conversation.() -> T): Flow<PagedList<T>> = CreatePagedList(
        config = pagedListConfig(pageSize = 20),
        dataSourceFactory = dao.dataSourceFactory().map(mapper)
    ).asFlow()
}