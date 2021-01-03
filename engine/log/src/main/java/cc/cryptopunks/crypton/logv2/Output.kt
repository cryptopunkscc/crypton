package cc.cryptopunks.crypton.logv2

typealias LogOutput = (LogEvent) -> Unit

class LogEvent(
    val timestamp: Long,
    val thread: String,
    val data: Any,
)
