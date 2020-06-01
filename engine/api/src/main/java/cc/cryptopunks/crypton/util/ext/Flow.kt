package cc.cryptopunks.crypton.util.ext

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


@Suppress("UNCHECKED_CAST")
fun <T : Any> Flow<T>.bufferedThrottle(wait: Long): Flow<List<T>> = channelFlow {
    val flush = Unit
    val channel = Channel<Any>()
    var job = launch { }
    launch {
        channel.consumeAsFlow().fold(emptyList<T>()) { buffer, next ->
            when (next) {
                flush -> emptyList<T>().also { send(buffer) }
                else -> buffer + next as T
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
