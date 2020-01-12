package cc.cryptopunks.crypton.context

import cc.cryptopunks.crypton.util.BroadcastErrorScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow

interface Service : CoroutineScope {

    val id: Any get() = Unit

    fun Binding.bind(): Job = launch { }

    interface Binding {
        val input: Flow<Any>
        val output: suspend (Any) -> Unit
        suspend fun Any.out() = output(this)
    }

    data class Result<S>(
        val action: Any,
        val state: S
    )

    class Scope : BroadcastErrorScope() {
        override val coroutineContext = SupervisorJob() + Dispatchers.IO
    }
}