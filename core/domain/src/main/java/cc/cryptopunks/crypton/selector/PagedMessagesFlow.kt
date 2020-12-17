package cc.cryptopunks.crypton.selector

import androidx.paging.PagedList
import cc.cryptopunks.crypton.context.ChatScope
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.chat
import cc.cryptopunks.crypton.context.messageRepo
import cc.cryptopunks.crypton.context.queryContext
import cc.cryptopunks.crypton.dep
import cc.cryptopunks.crypton.util.CreatePagedList
import cc.cryptopunks.crypton.util.MainExecutor
import cc.cryptopunks.crypton.util.asFlow
import cc.cryptopunks.crypton.util.pagedListConfig
import kotlinx.coroutines.flow.Flow

private val ChatScope.mainExecutor: MainExecutor by dep()

internal fun ChatScope.messagePagedListFlow(): Flow<PagedList<Message>> =
    CreatePagedList(
        config = pagedListConfig(pageSize = 20),
        dataSourceFactory = messageRepo.dataSourceFactory(chat.address),
        fetchExecutor = queryContext.executor,
        notifyExecutor = mainExecutor
    ).asFlow()
