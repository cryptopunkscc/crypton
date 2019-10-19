package cc.cryptopunks.crypton.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ConnectedCachedFlow<T>(
    scope: CoroutineScope,
    flow: Flow<T>
) : Flow<T> {
    private val channel = BroadcastChannel<T>(Channel.CONFLATED)
    init {
        scope.launch {
            flow.collect {
                channel.offer(it)
            }
        }
    }

    @InternalCoroutinesApi
    override suspend fun collect(collector: FlowCollector<T>) {
        channel.asFlow().collect(collector)
    }
}

fun <T> Flow<T>.connectedCache(
    scope: CoroutineScope
): Flow<T> = ConnectedCachedFlow(
    scope = scope,
    flow = this
)

fun <T> T.cache(): CacheFlow<T> = Impl(this)

interface CacheFlow<T> : Flow<T> {
    var value: T
    val set: suspend (T) -> Unit
    val get: suspend () -> T
    operator fun invoke() = value
    operator fun invoke(arg: T) {
        value = arg
    }

    operator fun invoke(reduce: T.() -> T) {
        value = value.reduce()
    }

    companion object {
        operator fun <T> invoke(
            initial: T
        ): CacheFlow<T> = Impl(initial)
    }
}

private class Impl<T>(
    initial: T
) : CacheFlow<T> {

    override val set: suspend (T) -> Unit = { arg: T -> value = arg }
    override val get: suspend () -> T = { value }

    private val channel = BroadcastChannel<T>(Channel.CONFLATED).apply {
        offer(initial)
    }

    override var value: T = initial
        set(value) {
            field = value
            channel.offer(value)
        }

    @InternalCoroutinesApi
    override suspend fun collect(collector: FlowCollector<T>) =
        channel.asFlow().collect(collector)
}