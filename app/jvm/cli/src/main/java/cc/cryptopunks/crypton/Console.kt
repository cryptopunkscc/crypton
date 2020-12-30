package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.cliv2.joinArgs
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flattenConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

fun consoleConnector(
    args: Array<String> = emptyArray()
): Connector = flowOf(
    args.argsFlow(),
    systemInput()
)
    .flattenConcat()
    .systemFlowConnector()

fun Flow<Any>.systemFlowConnector() = Connector(
    input = this,
    output = { println(this) }
)

fun Array<out Any>.argsFlow() = flowOf(joinArgs())

fun systemInput(): Flow<String> =
    flow {
        do {
            readLine()
                ?.let { emit(it) }
                ?: delay(100)
        } while (true)
    }
