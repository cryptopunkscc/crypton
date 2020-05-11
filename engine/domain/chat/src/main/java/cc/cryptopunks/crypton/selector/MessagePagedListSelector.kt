package cc.cryptopunks.crypton.selector

import androidx.paging.PagedList
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.Repo
import cc.cryptopunks.crypton.util.CreatePagedList
import cc.cryptopunks.crypton.util.MainExecutor
import cc.cryptopunks.crypton.util.asFlow
import cc.cryptopunks.crypton.util.pagedListConfig
import kotlinx.coroutines.flow.Flow

internal class MessagePagedListSelector(
    private val repo: Message.Repo,
    private val mainExecutor: MainExecutor,
    private val queryContext: Repo.Context.Query
) {
    operator fun invoke(
        chat: Chat
    ): Flow<PagedList<Message>> = CreatePagedList(
        config = pagedListConfig(pageSize = 20),
        dataSourceFactory = repo.dataSourceFactory(chat.address),
        fetchExecutor = queryContext.executor,
        notifyExecutor = mainExecutor
    ).asFlow()
}
