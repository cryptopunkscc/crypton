package cc.cryptopunks.crypton.util

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class BroadcastError: (Throwable) -> Unit, Flow<Throwable> {

    private val channel = BroadcastChannel<Throwable?>(Channel.CONFLATED)

    override fun invoke(throwable: Throwable) {
        GlobalScope.launch {
            channel.send(throwable)
            channel.offer(null)
        }
    }

    @InternalCoroutinesApi
    override suspend fun collect(collector: FlowCollector<Throwable>) {
        channel.asFlow().filterNotNull().collect(collector)
    }

    interface Component {
        val broadcastError: BroadcastError
    }

    class Module : Component {
        override val broadcastError = BroadcastError()
    }
}