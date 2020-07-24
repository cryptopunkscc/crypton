package cc.cryptopunks.crypton

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flowOf

typealias Output = suspend Any.() -> Unit

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
