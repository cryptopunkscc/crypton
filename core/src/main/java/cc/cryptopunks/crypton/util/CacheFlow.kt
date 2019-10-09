package cc.cryptopunks.crypton.util

import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.asFlow

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