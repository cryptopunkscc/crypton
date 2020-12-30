package cc.cryptopunks.crypton

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch

data class Connector(
    val input: Flow<Any>,
    val cancel: () -> Unit = {},
    val output: Output = {},
) {
    suspend fun Any.out() = output()
}

suspend fun Connector.connect(other: Connector) = coroutineScope {
    joinAll(
        launch { input.collect(other.output) },
        launch { other.input.collect(output) },
    )
}

fun Connector.logging() = copy(
    input = input.onEach { println("in: $it") },
    output = { println("out: $this"); out() }
)


