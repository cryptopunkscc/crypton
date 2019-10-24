package cc.cryptopunks.crypton.util

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

abstract class ErrorHandlingScope: CoroutineScope {
    abstract val broadcast: BroadcastError

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        broadcast(throwable)
    }

    infix fun launch(block: suspend CoroutineScope.() -> Unit): Job = launch(
        context = exceptionHandler,
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
}

open class BroadcastErrorScope :
    ErrorHandlingScope(),
    Flow<Throwable> {

    override val coroutineContext: CoroutineContext get() = TODO()

    override val broadcast = BroadcastError()

    @InternalCoroutinesApi
    override suspend fun collect(collector: FlowCollector<Throwable>) {
        broadcast.collect(collector)
    }
}