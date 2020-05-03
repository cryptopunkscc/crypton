package cc.cryptopunks.crypton.selector

import androidx.paging.PagedList
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.util.*
import kotlinx.coroutines.flow.Flow

internal class RosterSelector(
    private val repo: Chat.Repo,
    private val mainExecutor: MainExecutor,
    private val ioExecutor: IOExecutor
) {
    operator fun <T> invoke(mapper: (Chat) -> T): Flow<PagedList<out T>> = CreatePagedList(
        config = pagedListConfig(pageSize = 20),
        dataSourceFactory = repo.dataSourceFactory().map(mapper),
        notifyExecutor = mainExecutor,
        fetchExecutor = ioExecutor
    ).asFlow()
}
