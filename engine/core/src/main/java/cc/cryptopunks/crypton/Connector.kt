package cc.cryptopunks.crypton

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flattenConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch

data class TypedConnector<T>(
    val input: Flow<T>,
    val address: String = "",
    val cancel: () -> Unit = {},
    val output: TypedOutput<T> = {},
) {
    suspend fun T.out() = output()
}

typealias Connector = TypedConnector<Any>

fun emptyConnector() = Connector(emptyFlow())

suspend fun <T> TypedConnector<T>.connect(other: TypedConnector<T>) = coroutineScope {
    joinAll(
        launch { input.collect(other.output) },
        launch { other.input.collect(output) },
    )
}

fun <T> TypedConnector<T>.logging() = copy(
    input = input.onEach { println("in: $it") },
    output = { println("out: $this"); out() }
)

operator fun <T> TypedConnector<T>.plus(input: Flow<T>) = copy(input = input + this.input)

operator fun <T> Flow<T>.plus(other: Flow<T>): Flow<T> = flowOf(this, other).flattenConcat()
