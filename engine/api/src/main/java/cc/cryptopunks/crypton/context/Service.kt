package cc.cryptopunks.crypton.context

import cc.cryptopunks.crypton.util.BroadcastErrorScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow

interface Service : CoroutineScope {

    val id: Any get() = Unit

    class Scope : BroadcastErrorScope() {
        override val coroutineContext = SupervisorJob() + Dispatchers.IO
    }
}

typealias In = Connectable.Input
typealias Out = Connectable.Output

interface Connector {
    val input: Flow<Any>
    val output: suspend (Any) -> Unit
    suspend fun Any.out() = output(this)
}

interface Connectable : Service {
    interface Input
    interface Output

    fun Connector.connect(): Job = launch { }
    fun Connector.connect(service: Connectable): Job = launch { service.run { connect() }.join() }
}

interface Actor : Connectable {
    interface Status
    object Start : Status
    object Stop : Status
    object Connected : Status
}
