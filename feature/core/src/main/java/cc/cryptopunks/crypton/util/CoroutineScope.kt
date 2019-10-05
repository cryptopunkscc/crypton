package cc.cryptopunks.crypton.util

import kotlinx.coroutines.*

object Scopes {

    abstract class  ErrorHandling : CoroutineScope {
        abstract val broadcast: BroadcastError

        private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            broadcast(throwable)
        }

        infix fun launch(block: suspend CoroutineScope.() -> Unit) = launch(
            context = exceptionHandler,
            block = block
        )
    }

    class UseCase(
        override val broadcast: BroadcastError
    ) : ErrorHandling() {
        override val coroutineContext = SupervisorJob() + Dispatchers.IO
    }

    class ViewModel(
        override val broadcast: BroadcastError
    ) : ErrorHandling(),
        CoroutineScope by MainScope()
}