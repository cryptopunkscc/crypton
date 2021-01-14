package cc.cryptopunks.crypton

object RequestLog {

    class Event(
        request: RequestScope,
        val data: Any,
    ) : RequestScope by request {
        interface Status
        object Received : Status
        object Resolved : Status
        object Start : Status
        object Finish : Status
        data class Custom(val value: String) : Status
    }

    data class Data(
        val eventId: Long,
        val timestamp: Long,
        val time: Long = 0,
        val actionType: Class<*>,
        val scopes: List<String>,
        val data: Any,
    )
}
