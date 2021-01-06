package cc.cryptopunks.crypton.log

import cc.cryptopunks.crypton.RequestLog
import cc.cryptopunks.crypton.ScopeElement
import cc.cryptopunks.crypton.util.mapNotNull
import kotlinx.coroutines.CoroutineScope

fun RequestLog.Event.createRequestLogData(timestamp: Long) = RequestLog.Data(
    timestamp = timestamp,
    eventId = id,
    actionType = action.javaClass,
    scopes = scopes(),
    data = data
)

private fun CoroutineScope.scopes(): List<String> =
    coroutineContext.mapNotNull { (it as? ScopeElement)?.id }
