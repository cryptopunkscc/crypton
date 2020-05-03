package cc.cryptopunks.crypton.context

import cc.cryptopunks.crypton.util.BroadcastErrorScope
import cc.cryptopunks.crypton.util.OpenStore
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import java.lang.ref.WeakReference

interface Service : CoroutineScope {

    val id: Any get() = Unit

    class Scope : BroadcastErrorScope() {
        override val coroutineContext = SupervisorJob() + Dispatchers.IO
    }
}

typealias In = Connectable.Input
typealias Out = Connectable.Output

interface Connectable : Service {
    interface Input
    interface Output
    interface Binding : Connector {
        class Store : OpenStore<List<WeakReference<out Binding>>>(emptyList())

        val services: Set<Connectable>
        fun reserve(count: Int)
        suspend fun cancel()
        operator fun plus(service: Connectable?): Boolean
        operator fun minus(service: Connectable): Boolean
        operator fun plusAssign(service: Connectable)
    }

    fun Connector.connect(): Job = launch { }
    fun Connector.connect(service: Connectable): Job = launch {
        service.run {
            connect()
        }.join()
    }
}

interface Actor : Connectable {
    interface Status
    object Start : Status
    object Stop : Status
    object Connected : Status
}

interface Connector {
    val input: Flow<Any>
    val output: suspend (Any) -> Unit
    suspend fun Any.out() = output(this)
}
