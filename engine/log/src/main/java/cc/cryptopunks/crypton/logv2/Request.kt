package cc.cryptopunks.crypton.logv2

data class LogRequest(
    val timestamp: Long = System.currentTimeMillis(),
    val thread: String = Thread.currentThread().name,
    val get: GetLogData,
)

typealias GetLogData = () -> Any
