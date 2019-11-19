package cc.cryptopunks.crypton.util

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

open class Broadcast<T: Any> : (T) -> Unit, Flow<T> {

    private val channel = BroadcastChannel<T?>(Channel.CONFLATED)

    override fun invoke(value: T) {
        GlobalScope.launch {
            channel.offer(value)
        }
    }

    suspend fun send(value: T) {
        channel.send(value)
    }

    @InternalCoroutinesApi
    override suspend fun collect(collector: FlowCollector<T>) {
        channel.asFlow().filterNotNull().collect(collector)
    }
}