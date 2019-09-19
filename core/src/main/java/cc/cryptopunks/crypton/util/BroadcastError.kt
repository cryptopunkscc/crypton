package cc.cryptopunks.crypton.util

import dagger.Provides
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filterNotNull
import timber.log.Timber

interface BroadcastError : (Throwable) -> Unit, Flow<Throwable> {

    @FlowPreview
    @ExperimentalCoroutinesApi
    private class Impl : BroadcastError {

        private val channel = BroadcastChannel<Throwable?>(Channel.CONFLATED)

        override fun invoke(throwable: Throwable) {
            GlobalScope.launch {
                channel.send(throwable)
                Timber.e(throwable)
                channel.offer(null)
            }
        }

        @InternalCoroutinesApi
        override suspend fun collect(collector: FlowCollector<Throwable>) {
            channel.asFlow().filterNotNull().collect(collector)
        }
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    @dagger.Module
    class Module {
        private val impl = Impl()
        @Provides
        fun broadcastError(): BroadcastError = impl
    }

    interface Component {
        val broadcastError: BroadcastError
    }
}