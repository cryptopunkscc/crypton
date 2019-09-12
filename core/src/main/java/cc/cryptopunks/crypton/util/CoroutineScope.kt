package cc.cryptopunks.crypton.util

import cc.cryptopunks.crypton.module.ViewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import javax.inject.Inject
import javax.inject.Singleton

object Scopes {

    @Singleton
    class Data @Inject constructor(): CoroutineScope by CoroutineScope(Dispatchers.IO)

    @Singleton
    class Command @Inject constructor(): CoroutineScope by CoroutineScope(Dispatchers.Default)

    @ViewModelScope
    class ViewModel @Inject constructor(): CoroutineScope by MainScope()

    interface View: CoroutineScope {
        private class Impl : View, CoroutineScope by MainScope()
        companion object : () -> View by { Impl() }
    }
}