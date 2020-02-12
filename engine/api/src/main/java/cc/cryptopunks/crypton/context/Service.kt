package cc.cryptopunks.crypton.context

import cc.cryptopunks.crypton.util.BroadcastErrorScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow

interface Service : CoroutineScope {
    interface Connectable : Service {
        interface Input
        interface Output
    }
    interface Actor : Connectable {
        interface Status
        object Start : Status
        object Stop : Status
        object Connected : Status
    }

    val id: Any get() = Unit

    fun Connector.connect(): Job = launch { }
    fun Connector.connect(service: Service): Job = launch { service.run { connect() }.join() }

    interface Connector {
        val input: Flow<Any>
        val output: suspend (Any) -> Unit
        suspend fun Any.out() = output(this)
    }

    class Scope : BroadcastErrorScope() {
        override val coroutineContext = SupervisorJob() + Dispatchers.IO
    }
}

typealias Connectable = Service.Connectable
typealias In = Service.Connectable.Input
typealias Out = Service.Connectable.Output