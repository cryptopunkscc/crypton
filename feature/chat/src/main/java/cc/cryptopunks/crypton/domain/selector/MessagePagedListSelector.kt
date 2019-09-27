package cc.cryptopunks.crypton.domain.selector

import androidx.paging.PagedList
import cc.cryptopunks.crypton.entity.Message
import cc.cryptopunks.crypton.util.CreatePagedList
import cc.cryptopunks.crypton.util.asFlow
import cc.cryptopunks.crypton.util.pagedListConfig
import kotlinx.coroutines.flow.Flow

class MessagePagedListSelector(
    private val dao: Message.Dao
) {
    operator fun <T> invoke(mapper: Message.() -> T): Flow<PagedList<T>> = CreatePagedList(
        config = pagedListConfig(pageSize = 20),
        dataSourceFactory = dao.dataSourceFactory().map(mapper)
    ).asFlow()
}