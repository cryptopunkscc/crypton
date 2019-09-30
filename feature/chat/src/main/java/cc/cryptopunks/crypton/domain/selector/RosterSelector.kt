package cc.cryptopunks.crypton.domain.selector

import androidx.paging.PagedList
import cc.cryptopunks.crypton.entity.Chat
import cc.cryptopunks.crypton.util.CreatePagedList
import cc.cryptopunks.crypton.util.asFlow
import cc.cryptopunks.crypton.util.pagedListConfig
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

data class RosterSelector @Inject constructor(
    private val repo: Chat.Repo
) {
    operator fun <T> invoke(mapper: Chat.() -> T): Flow<PagedList<T>> = CreatePagedList(
        config = pagedListConfig(pageSize = 20),
        dataSourceFactory = repo.dataSourceFactory().map(mapper)
    ).asFlow()
}