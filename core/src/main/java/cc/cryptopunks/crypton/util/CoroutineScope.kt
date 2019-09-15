package cc.cryptopunks.crypton.util

import cc.cryptopunks.crypton.module.FeatureScope
import cc.cryptopunks.crypton.module.ViewModelScope
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.EmptyCoroutineContext

object Scopes {

    interface ErrorHandling : CoroutineScope {
        val handle: HandleError
        fun launch(block: suspend CoroutineScope.() -> Unit) = launch(
            context = EmptyCoroutineContext,
            block = block
        ).apply {
            invokeOnCompletion { error ->
                if (error != null)
                    handle(error)
            }
        }
    }

    @Singleton
    class UseCase @Inject constructor(
        override val handle: HandleError
    ) : ErrorHandling {
        override val coroutineContext = SupervisorJob() + Dispatchers.IO
    }

    @FeatureScope
    class Feature @Inject constructor(
        override val handle: HandleError
    ) : ErrorHandling,
        CoroutineScope by MainScope()

    @ViewModelScope
    class ViewModel @Inject constructor(
        override val handle: HandleError
    ) : ErrorHandling,
        CoroutineScope by MainScope()
}