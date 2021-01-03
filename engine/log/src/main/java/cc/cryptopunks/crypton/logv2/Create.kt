package cc.cryptopunks.crypton.logv2

import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

fun <S, D : Any> createLog(
    create: CreateLogData<S, D>,
): LogElement<S, D> = LogElement { level, build ->
    if (level >= LogScope.level) LogRequest { create(build) }.dispatch()
}

typealias CreateLogData<S, D> = (BuildLogData<S, D>) -> Any

private fun LogRequest.dispatch(): Job =
    LogScope.launch { LogScope(LogEvent(timestamp, thread, get())) }
