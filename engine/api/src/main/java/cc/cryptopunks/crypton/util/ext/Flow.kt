package cc.cryptopunks.crypton.util.ext

import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

//inline fun <T, R> Flow<T>.map(crossinline transform: (value: T) -> R): Flow<R> = map { transform(it) }


fun <T : Any> Flow<T>.bufferedThrottle(wait: Long): Flow<List<T>> = channelFlow {
    val flush = Unit
    var buffer = emptyList<T>()
    val channel = Channel<Any>()
    var job: Job = launch { }
    launch {
        channel.consumeEach {
            buffer = if (it != flush) {
                buffer + it as T
            } else {
                send(buffer)
                emptyList<T>()
            }
        }
    }
    collect {
        job.cancel()
        channel.send(it)
        job = launch {
            delay(wait)
            channel.send(flush)
        }
    }
    job.join()
}
