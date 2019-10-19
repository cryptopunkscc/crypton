package cc.cryptopunks.crypton.util

import androidx.paging.DataSource
import androidx.paging.PagedList
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.onCompletion
import java.util.concurrent.Executor

fun pagedListConfig(
    pageSize: Int,
    prefetchDistance: Int = -1,
    initialLoadSizeHint: Int = -1,
    enablePlaceholders: Boolean = true,
    maxSize: Int = PagedList.Config.MAX_SIZE_UNBOUNDED
) = PagedList.Config
    .Builder()
    .setPageSize(pageSize)
    .setMaxSize(maxSize)
    .setPrefetchDistance(prefetchDistance)
    .setInitialLoadSizeHint(initialLoadSizeHint)
    .setEnablePlaceholders(enablePlaceholders)
    .build()

fun <K, V> CreatePagedList<K, V>.asFlow(): Flow<PagedList<V>> = PagedListFlow(this)
    .run { onCompletion { cancel() } }

class CreatePagedList<K, V>(
    private val config: PagedList.Config,
    private val dataSourceFactory: DataSource.Factory<K, V>,
    private val boundaryCallback: PagedList.BoundaryCallback<out V>? = null,
    private val notifyExecutor: Executor,
    internal val fetchExecutor: Executor
) {

    operator fun invoke(key: K? = null): PagedList<V> = create(pagedList(key))

    fun <V2> map(f: (V) -> V2) = CreatePagedList<K, V2>(
        config = config,
        dataSourceFactory = dataSourceFactory.map(f),
        fetchExecutor = fetchExecutor,
        notifyExecutor = notifyExecutor
    )

    private tailrec fun create(
        list: PagedList<V>
    ): PagedList<V> = if (!list.isDetached)
        list else
        create(pagedList(list.lastKey as K?))

    private fun pagedList(key: K?) = PagedList
        .Builder(dataSourceFactory.create(), config)
        .setInitialKey(key)
        .setNotifyExecutor(notifyExecutor)
        .setFetchExecutor(fetchExecutor)
        .setBoundaryCallback(boundaryCallback)
        .build()
}

@ExperimentalCoroutinesApi
private class PagedListFlow<K, V>(
    private val createPagedList: CreatePagedList<K, V>
) : Flow<PagedList<V>> {

    private val channel = Channel<PagedList<V>>(1)

    private val invalidationCallback = DataSource.InvalidatedCallback {
        if (!channel.isClosedForSend) createPagedList.fetchExecutor.execute {
            channel.offer(
                createPagedList(lastKey).also {
                    lastPagedList = it
                }
            )
        }
    }

    private val lastKey: K? get() = lastPagedList?.lastKey as? K?

    private var lastPagedList: PagedList<V>? = null
        set(value) {
            field?.dataSource?.removeInvalidatedCallback(invalidationCallback)
            value?.dataSource?.addInvalidatedCallback(invalidationCallback)
            field = value
        }

    @FlowPreview
    @InternalCoroutinesApi
    override suspend fun collect(collector: FlowCollector<PagedList<V>>) {
        invalidationCallback.onInvalidated()
        channel.consumeAsFlow().collect(collector)
    }

    fun cancel() {
        lastPagedList = null
        channel.cancel()
    }
}