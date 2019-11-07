package cc.cryptopunks.crypton.util

import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

class Broadcast<T: Any> : Flow<T> {

    private val channel = BroadcastChannel<T?>(Channel.CONFLATED)

    operator fun invoke(value: T) = synchronized(this) {
        channel.offer(value)
        channel.offer(null)
    }

    suspend fun send(value: T) {
        channel.send(value)
    }

    @InternalCoroutinesApi
    override suspend fun collect(collector: FlowCollector<T>) {
        channel.asFlow().filterNotNull().collect(collector)
    }
}