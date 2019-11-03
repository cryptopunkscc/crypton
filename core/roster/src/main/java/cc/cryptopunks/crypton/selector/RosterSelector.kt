package cc.cryptopunks.crypton.selector

import androidx.paging.PagedList
import cc.cryptopunks.crypton.entity.Chat
import cc.cryptopunks.crypton.util.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

data class RosterSelector @Inject constructor(
    private val repo: Chat.Repo,
    private val mainExecutor: MainExecutor,
    private val ioExecutor: IOExecutor
) {
    operator fun <T> invoke(mapper: Chat.() -> T): Flow<PagedList<T>> = CreatePagedList(
        config = pagedListConfig(pageSize = 20),
        dataSourceFactory = repo.dataSourceFactory().map(mapper),
        notifyExecutor = mainExecutor,
        fetchExecutor = ioExecutor
    ).asFlow()
}