package cc.cryptopunks.crypton.selector

import androidx.paging.PagedList
import cc.cryptopunks.crypton.context.ChatScope
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.util.CreatePagedList
import cc.cryptopunks.crypton.util.asFlow
import cc.cryptopunks.crypton.util.pagedListConfig
import kotlinx.coroutines.flow.Flow

internal fun ChatScope.messagePagedListFlow(): Flow<PagedList<Message>> =
    CreatePagedList(
        config = pagedListConfig(pageSize = 20),
        dataSourceFactory = messageRepo.dataSourceFactory(chat.address),
        fetchExecutor = queryContext.executor,
        notifyExecutor = mainExecutor
    ).asFlow()
