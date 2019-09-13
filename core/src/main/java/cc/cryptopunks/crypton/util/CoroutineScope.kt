package cc.cryptopunks.crypton.util

import cc.cryptopunks.crypton.module.ViewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.EmptyCoroutineContext

object Scopes {

    @Singleton
    class Data @Inject constructor() : CoroutineScope by CoroutineScope(Dispatchers.IO)

    @Singleton
    class Feature @Inject constructor(
        private val handleError: HandleError
    ) : CoroutineScope by CoroutineScope(Dispatchers.IO) {
        fun launch(block: suspend CoroutineScope.() -> Unit) = launch(
            context = EmptyCoroutineContext,
            block = block
        ).apply {
            invokeOnCompletion { error ->
                if (error != null)
                    handleError(error)
            }
        }
    }

    @ViewModelScope
    class ViewModel @Inject constructor() : CoroutineScope by MainScope()

    interface View : CoroutineScope {
        private class Impl : View, CoroutineScope by MainScope()
        companion object : () -> View by { Impl() }
    }
}