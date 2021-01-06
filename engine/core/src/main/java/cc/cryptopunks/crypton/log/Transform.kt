package cc.cryptopunks.crypton.log

import cc.cryptopunks.crypton.RequestLog
import cc.cryptopunks.crypton.logv2.LogEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull

fun Flow<LogEvent>.mapToRequestLogData():
    Flow<RequestLog.Data> =
    mapNotNull(toRequestLogData)

val toRequestLogData:
    suspend LogEvent.() -> RequestLog.Data? =
    { (data as? RequestLog.Event)?.createRequestLogData(timestamp) }


fun Flow<RequestLog.Data>.groupById(
    events: MutableMap<Long, MutableList<RequestLog.Data>> = mutableMapOf(),
):
    Flow<List<RequestLog.Data>> =
    map(events.createGroupById())

fun MutableMap<Long, MutableList<RequestLog.Data>>.createGroupById():
    suspend RequestLog.Data.() -> List<RequestLog.Data> =
    {
        when (data) {
            is RequestLog.Event.Finish,
            is Throwable,
            -> remove(eventId) ?: mutableListOf()
            else
            -> getOrPut(eventId) { mutableListOf() }
        }.also { group ->
            group += if (isEmpty()) this else copy(time = timestamp - group.first().timestamp)
        }
    }
