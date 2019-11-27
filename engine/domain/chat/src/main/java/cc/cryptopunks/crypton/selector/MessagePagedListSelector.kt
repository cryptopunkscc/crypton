package cc.cryptopunks.crypton.selector

import androidx.paging.PagedList
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.util.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MessagePagedListSelector @Inject constructor(
    private val repo: Message.Repo,
    private val mainExecutor: MainExecutor,
    private val ioExecutor: IOExecutor
) {
    operator fun <T> invoke(
        chat: Chat,
        mapper: Message.() -> T
    ): Flow<PagedList<T>> = CreatePagedList(
        config = pagedListConfig(pageSize = 20),
        dataSourceFactory = repo.dataSourceFactory(chat).map(mapper),
        fetchExecutor = ioExecutor,
        notifyExecutor = mainExecutor
    ).asFlow()
}