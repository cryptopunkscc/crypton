package cc.cryptopunks.crypton.log

import cc.cryptopunks.crypton.RequestLog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter

fun Flow<List<RequestLog.Data>>.filterLast() = filter(isLast)

val isLast: suspend List<RequestLog.Data>.() -> Boolean = {
    when (last().data) {
        is RequestLog.Event.Finish,
        is Throwable,
        -> true
        else
        -> false
    }
}
