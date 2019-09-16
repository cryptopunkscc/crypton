package cc.cryptopunks.crypton.util

import cc.cryptopunks.crypton.module.FeatureScope
import cc.cryptopunks.crypton.module.ViewModelScope
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Singleton

object Scopes {

    abstract class  ErrorHandling : CoroutineScope {
        abstract val handle: HandleError

        private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            handle(throwable)
        }

        fun launch(block: suspend CoroutineScope.() -> Unit) = launch(
            context = exceptionHandler,
            block = block
        )
    }

    @Singleton
    class UseCase @Inject constructor(
        override val handle: HandleError
    ) : ErrorHandling() {
        override val coroutineContext = SupervisorJob() + Dispatchers.IO
    }

    @FeatureScope
    class Feature @Inject constructor(
        override val handle: HandleError
    ) : ErrorHandling(),
        CoroutineScope by MainScope()

    @ViewModelScope
    class ViewModel @Inject constructor(
        override val handle: HandleError
    ) : ErrorHandling(),
        CoroutineScope by MainScope()
}