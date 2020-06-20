package cc.cryptopunks.crypton.context

import cc.cryptopunks.crypton.util.OpenStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

interface Service : CoroutineScope {
    val id: Any get() = this::class.java.simpleName
}

interface Subscription {
    val enable: Boolean
}

interface Connectable : Service {
    interface Binding {
        class Store : OpenStore<List<WeakReference<out Binding>>>(emptyList())

        val services: Set<Connectable>
        suspend fun cancel()
        operator fun plus(service: Connectable?): Boolean
        operator fun minus(service: Connectable): Boolean
        fun send(any: Any) = Unit
    }

    fun Connector.connect(): Job = launch { }
    fun Connector.connect(service: Connectable): Job = launch {
        service.run { connect() }.join()
    }
}

interface Actor : Connectable {
    interface Status
    object Start : Status
    object Stop : Status
    object Connected : Status

    companion object {
        val Empty = object : Actor {
            override val coroutineContext get() = Dispatchers.Unconfined
        }
    }
}

data class Connector(
    val input: Flow<Any>,
    val output: suspend (Any) -> Unit
) {
    suspend fun Any.out() = output(this)
}

typealias ConnectorOutput = suspend (Any) -> Unit

fun Connector.actor(): Actor = object : Actor, Connectable by ConnectableConnector(this) {}

private class ConnectableConnector(
    private val connector: Connector
) : Connectable {
    override val coroutineContext = SupervisorJob() + Dispatchers.Unconfined
    override fun Connector.connect(): Job = launch {
        launch { input.collect(connector.output) }
        launch { connector.input.collect(output) }
    }
}
