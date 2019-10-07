package cc.cryptopunks.crypton.util

import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.asFlow

fun <T> T.cache() = Cache(this)

interface Cache<T> : Flow<T> {
    var value: T
    val set: suspend (T) -> Unit
    val get: suspend () -> T
    operator fun invoke() = value
    operator fun invoke(arg: T) {
        value = arg
    }

    operator fun invoke(reduce: T.() -> T) = value.reduce().also { value = it }

    companion object {
        operator fun <T> invoke(
            initial: T
        ): Cache<T> = Impl(initial)
    }

    private class Impl<T>(
        initial: T
    ) : Cache<T> {

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
}