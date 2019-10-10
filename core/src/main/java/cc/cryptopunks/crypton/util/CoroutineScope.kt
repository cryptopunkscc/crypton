package cc.cryptopunks.crypton.util

import kotlinx.coroutines.*
import kotlin.coroutines.EmptyCoroutineContext

object Scope {

    abstract class ErrorHandling internal constructor(): CoroutineScope {
        abstract val broadcast: BroadcastError

        private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            broadcast(throwable)
        }

        infix fun launch(block: suspend CoroutineScope.() -> Unit) = launch(
            context = exceptionHandler,
            block = block
        )

        infix fun <T> async(block: suspend CoroutineScope.() -> T) = async(
            context = EmptyCoroutineContext,
            block = block
        ).apply {
            invokeOnCompletion { cause: Throwable? ->
                cause?.let(broadcast)
            }
        }
    }

    class UseCase(
        override val broadcast: BroadcastError
    ) : ErrorHandling() {
        override val coroutineContext = SupervisorJob() + Dispatchers.IO
    }

    class Presentation : CoroutineScope by MainScope()

    class View : CoroutineScope by MainScope()
}