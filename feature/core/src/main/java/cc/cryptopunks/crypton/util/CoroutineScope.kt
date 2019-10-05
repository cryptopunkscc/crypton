package cc.cryptopunks.crypton.util

import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Singleton

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

    @Singleton
    class UseCase @Inject constructor(
        override val broadcast: BroadcastError
    ) : ErrorHandling() {
        override val coroutineContext = SupervisorJob() + Dispatchers.IO
    }

    class ViewModel @Inject constructor(
        override val broadcast: BroadcastError
    ) : ErrorHandling(),
        CoroutineScope by MainScope()
}