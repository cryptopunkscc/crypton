package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.Action
import cc.cryptopunks.crypton.Connector
import cc.cryptopunks.crypton.Execute
import cc.cryptopunks.crypton.Output
import cc.cryptopunks.crypton.Request
import cc.cryptopunks.crypton.Service
import cc.cryptopunks.crypton.execute.handleRequest
import cc.cryptopunks.crypton.execute.resolveAction
import cc.cryptopunks.crypton.execute.resolveHandle
import cc.cryptopunks.crypton.execute.resolveScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlin.coroutines.coroutineContext

internal val defaultExecution = listOf(
    resolveAction,
    resolveScope,
    resolveHandle,
    handleRequest
)

suspend fun Service.start(
    execution: List<Execute> = defaultExecution,
) {
    input.consumeAsFlow().start(
        execution = execution,
        output = output::send
    )
}

suspend fun Flow<Any>.start(
    execution: List<Execute> = defaultExecution,
    output: Output,
) {
    val base = Request(
        out = output,
        root = CoroutineScope(coroutineContext),
    )

    this.onStart { println("Start service") }
        .onCompletion { println("Stop service") }
        .collect { input: Any ->
            execution.fold(
                base.copy(arg = input)
            ) { request, execute: Execute ->
                execute(request)
            }
        }
}

fun Service.terminate() {
    input.cancel()
    output.cancel()
}

fun Service.connector() = Connector(
    input = output.asFlow(),
    output = { input.send(this as Action) }
)
