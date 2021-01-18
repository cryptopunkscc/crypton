package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.Action
import cc.cryptopunks.crypton.Execute
import cc.cryptopunks.crypton.Execution
import cc.cryptopunks.crypton.Output
import cc.cryptopunks.crypton.Request
import cc.cryptopunks.crypton.RequestLog
import cc.cryptopunks.crypton.Service
import cc.cryptopunks.crypton.TypedConnector
import cc.cryptopunks.crypton.TypedOutput
import cc.cryptopunks.crypton.execute.defaultExecution
import cc.cryptopunks.crypton.logv2.d
import cc.cryptopunks.crypton.nextId
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.supervisorScope

suspend fun <T: Any> Flow<T>.start(
    execution: Execution = defaultExecution,
    output: TypedOutput<T> = {},
) = supervisorScope {
    Request(
        action = Service.Running,
        out = { output(this as T) }, // TODO
        root = this,
    ).run {
        log.d { "Start service" }
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

private fun <T: Any> Request.new(input: T) = copy(
    id = Request.nextId(),
    arg = input,
    action = Action.Empty
).apply {
    log.d { RequestLog.Event.Received }
}

suspend fun <T: Any> TypedConnector<T>.start(
    execution: Execution = defaultExecution,
) {
    input.start(execution, output)
}

suspend fun Action.start(
    execution: Execution = defaultExecution,
    output: Output = {},
) = flowOf(this).start(execution, output)

suspend fun Service.start(
    execution: Execution = defaultExecution,
) {
    input.consumeAsFlow().start(
        execution = execution,
        output = { output.send(this) }
    )
}

fun Service.terminate() {
    input.cancel()
    output.cancel()
}
