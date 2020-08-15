package cc.cryptopunks.crypton

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

interface Actor : Connectable

fun actor(
    context: CoroutineContext = Dispatchers.Unconfined,
    onConnect: suspend (Connector) -> Unit
) = object : Actor {
    override val coroutineContext = SupervisorJob() + context
    override fun Connector.connect(): Job = launch { onConnect(this@connect) }
}

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
