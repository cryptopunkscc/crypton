package cc.cryptopunks.crypton.context

import cc.cryptopunks.crypton.util.BroadcastErrorScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow

interface Service : CoroutineScope {
    interface Connectable : Service
    interface Actor : Connectable

    val id: Any get() = Unit

    fun Connector.connect(): Job = launch { }
    fun Connector.connect(service: Service): Job = launch { service.run { connect() }.join() }

    interface Connector {
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