package cc.cryptopunks.crypton.logv2

import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

fun <S, D : Any> createLog(
    createData: CreateLogData<S, D>,
): LogElement<S, D> = LogElement { level, builder ->
    if (level >= LogScope.level) dispatchLog { createData(builder) }
}

typealias CreateLogData<S, D> = (BuildLogData<S, D>) -> Any

private fun dispatchLog(
    timestamp: Long = System.currentTimeMillis(),
    thread: String = Thread.currentThread().name,
    createData: () -> Any,
): Job = LogScope.launch {
    LogScope(LogEvent(timestamp, thread, createData()))
}
