package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.Action
import cc.cryptopunks.crypton.Connector
import cc.cryptopunks.crypton.Execute
import cc.cryptopunks.crypton.Execution
import cc.cryptopunks.crypton.Output
import cc.cryptopunks.crypton.Request
import cc.cryptopunks.crypton.RequestLog
import cc.cryptopunks.crypton.Service
import cc.cryptopunks.crypton.execute.defaultExecution
import cc.cryptopunks.crypton.logv2.d
import cc.cryptopunks.crypton.nextId
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.supervisorScope

suspend fun Flow<Any>.start(
    execution: Execution = defaultExecution,
    output: Output = {},
) = supervisorScope {
    Request(
        action = Service.Running,
        out = output,
        root = this,
    ).run {
        log.d { RequestLog.Event.Custom("Start service") }
        collect { input: Any ->
            execution.fold(
                new(input)
            ) { request: Request, execute: Execute ->
                execute(request)
            }
        }
        coroutineContext[Job]?.join()
        log.d { "Stop service" }
    }
}

private fun Request.new(input: Any) = copy(
    id = Request.nextId(),
    arg = input,
    action = Action.Empty
).apply {
    log.d { RequestLog.Event.Received }
}

suspend fun Connector.start(
    execution: Execution = defaultExecution,
) {
    input.start(execution, output)
}

suspend fun Any.start(
    execution: Execution = defaultExecution,
    output: Output = {},
) = flowOf(this).start(execution, output)

suspend fun Service.start(
    execution: Execution = defaultExecution,
) {
    input.consumeAsFlow().start(
        execution = execution,
        output = output::send
    )
}

fun Service.terminate() {
    input.cancel()
    output.cancel()
}
