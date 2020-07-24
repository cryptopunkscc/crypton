package cc.cryptopunks.crypton

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

data class Connector(
    val input: Flow<Any>,
    val close: () -> Unit = {},
    val output: suspend (Any) -> Unit = {}
) {
    suspend fun Any.out() = output(this)
}

fun Any.connector(
    output: suspend (Any) -> Unit = {}
) = Connector(
    input = flowOf(this),
    output = output
)

fun connector(
    vararg args: Any,
    output: suspend (Any) -> Unit = {}
) = Connector(
    input = args.asFlow(),
    output = output
)

typealias ConnectorOutput = suspend Any.() -> Unit

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
