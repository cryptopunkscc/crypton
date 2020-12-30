package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.Connector
import cc.cryptopunks.crypton.Execute
import cc.cryptopunks.crypton.Execution
import cc.cryptopunks.crypton.Output
import cc.cryptopunks.crypton.Request
import cc.cryptopunks.crypton.Service
import cc.cryptopunks.crypton.execute.defaultExecution
import cc.cryptopunks.crypton.nextId
import cc.cryptopunks.crypton.util.logger.log
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
    val base = Request(
        out = output,
        root = this,
    )
    //    base.log { "Start service" }
    log.d { "Start service" }
    collect { input: Any ->
        execution.fold(
            base.copy(
                id = Request.nextId(),
                arg = input,
            )
        ) { request: Request, execute: Execute ->
            execute(request)
        }
    }
    coroutineContext[Job]?.join()
//    base.log { "Stop service" }
    log.d { "Stop service" }
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
