package cc.cryptopunks.crypton.util

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collect
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

open class BroadcastErrorScope :
    CoroutineScope,
    Flow<Throwable> {

    private val log = typedLog()

    override val coroutineContext: CoroutineContext get() = TODO()

    private val broadcast = BroadcastError()

    private val errorHandlingContext: CoroutineContext = CoroutineExceptionHandler { _, throwable ->
        log.e(throwable)
        broadcast(throwable)
    }

    infix fun launch(block: suspend CoroutineScope.() -> Unit): Job = launch(
        context = errorHandlingContext,
        block = block
    )

    infix fun <T> async(block: suspend CoroutineScope.() -> T): Deferred<T> = async(
        context = EmptyCoroutineContext,
        block = block
    ).apply {
        invokeOnCompletion { cause: Throwable? ->
            cause?.let(broadcast)
        }
    }

    @InternalCoroutinesApi
    override suspend fun collect(collector: FlowCollector<Throwable>) {
        broadcast.collect(collector)
    }
}

fun BroadcastErrorScope.bind(broadcastError: BroadcastError) =
    launch { collect { broadcastError.send(it) } }